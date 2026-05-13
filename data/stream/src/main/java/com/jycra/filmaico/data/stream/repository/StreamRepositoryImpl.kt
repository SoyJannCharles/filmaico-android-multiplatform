package com.jycra.filmaico.data.stream.repository

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.data.stream.data.dao.StreamCacheDao
import com.jycra.filmaico.data.stream.data.provider.internal.StreamCredentialsProvider
import com.jycra.filmaico.data.stream.entity.StreamCacheEntity
import com.jycra.filmaico.data.stream.util.StreamSourceType
import com.jycra.filmaico.data.stream.util.StreamUrl
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import com.jycra.filmaico.domain.stream.util.MediaType
import com.jycra.filmaico.domain.stream.model.PlaybackParams
import com.jycra.filmaico.domain.stream.model.DrmContent
import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.model.Key
import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.ResolutionResult
import com.jycra.filmaico.domain.stream.model.Stream
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.repository.StreamRepository
import com.jycra.filmaico.domain.stream.resolver.FlowStreamResolver
import com.jycra.filmaico.domain.stream.resolver.BaseStreamResolver
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

@Singleton
class StreamRepositoryImpl @Inject constructor(
    private val flowStreamResolver: FlowStreamResolver,
    private val baseStreamResolver: BaseStreamResolver,
    private val streamCredentialsProvider: StreamCredentialsProvider,
    private val streamCacheDao: StreamCacheDao,
) : StreamRepository {

    override suspend fun resolveStream(
        resolutionParams: ResolutionParams,
        lastSession: ResolutionSession?,
        onStateChange: (StreamExtractionState) -> Unit
    ): Flow<ResolutionResult> = flow {

        when (resolutionParams.source) {
            is Stream.Direct -> {
                emit(resolveDirectSource(resolutionParams, lastSession, onStateChange))
            }
            is Stream.WebViewScrap -> {
                emit(resolveWebViewScrapSource(resolutionParams))
            }
        }

    }

    private suspend fun resolveDirectSource(
        params: ResolutionParams,
        lastSession: ResolutionSession?,
        onStateChange: (StreamExtractionState) -> Unit
    ): ResolutionResult {

        val source = params.source as Stream.Direct

        val session = if (source.isFlow()) {
            flowStreamResolver.resolve(params, lastSession, onStateChange)
        } else null

        val cookieUrl = source.cookieUrl
        val headers = source.headers
        val drmContent = source.drmContent

        val resolvedCookies = getCookies(cookieUrl)
        val resolvedHeaders = getHeaders(headers, resolvedCookies)
        val resolvedDrm = drmContent?.let { getDrmKeys(it) }

        return ResolutionResult(
            session,
            PlaybackParams(
                uri = session?.resolvedUrl ?: source.uri,
                headers = resolvedHeaders,
                keys = resolvedDrm,
            )
        )

    }

    private suspend fun resolveWebViewScrapSource(resolutionParams: ResolutionParams): ResolutionResult {

        val source = resolutionParams.source as Stream.WebViewScrap

        val session = if (source.shouldUseEvalResolver()) {
            baseStreamResolver.resolve(source.iframeUrl)
        } else {
            baseStreamResolver.resolveWithWebView(source.iframeUrl)
        }

        return ResolutionResult(
            session,
            PlaybackParams(
                uri = session?.resolvedUrl ?: source.iframeUrl,
                headers = null,
                keys = null,
            )
        )

    }

    private suspend fun getCookies(
        url: String?,
        forceRefresh: Boolean = false,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): String? = withContext(Dispatchers.IO) {

        if (url.isNullOrBlank()) return@withContext null

        coroutineContext.ensureActive()

        try {
            val cookie = streamCredentialsProvider.getCookies(url, forceRefresh)
            if (!cookie.isNullOrBlank()) {
                FLog.d(LogCategory.NETWORK, "Session established via cookies for ${url.toHostOnly()}")
            }
            cookie
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            FLog.e(LogCategory.NETWORK, "Cookie Fetch Error", e)
            null
        }

    }

    private fun getHeaders(
        headers: Map<String, String>?,
        cookies: String?,
    ): Map<String, String>? {

        val resolvedHeaders = mutableMapOf<String, String>()

        headers?.let {
            resolvedHeaders.putAll(it)
        }

        cookies?.let { resolvedHeaders.put("Cookie", it) }

        return resolvedHeaders

    }

    private suspend fun getDrmKeys(
        drmContent: DrmContent,
        forceRefresh: Boolean = false,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): DrmKeys? {

        coroutineContext.ensureActive()

        if (!forceRefresh && drmContent.staticKeys.isValid()) {
            return DrmKeys(keys = listOf(
                Key(kty = "oct", k = drmContent.staticKeys.k, kid = drmContent.staticKeys.kid)
            ))
        }

        if (drmContent.licenseUrl.isNotBlank()) {
            return try {
                streamCredentialsProvider.getDrmKeys(drmContent.licenseUrl)
            } catch (e: Exception) {
                FLog.e(LogCategory.NETWORK, "DRM License Request Failed", e)
                null
            }
        }

        return null

    }

    override suspend fun reportSuccess(session: ResolutionSession) {

        val sourceType = StreamUrl.getSourceType(url = session.streamUrl ?: "")

        when (sourceType) {
            StreamSourceType.FLOW -> {
                FLog.d(LogCategory.NETWORK, "[Lvl 3] Confirming Flow Edge Health: ${session.host}")
            }

            StreamSourceType.SEED -> {
                FLog.d(LogCategory.SCRAPER, "[Lvl 3] Confirming Seed Domain Health: ${session.host}")
                baseStreamResolver.reportSeedSuccess(session)
            }

            else -> {
                FLog.d(LogCategory.NETWORK, "[Lvl 3] Base Stream playing correctly from: ${session.host}")
            }
        }

    }

    override suspend fun reportFailure(session: ResolutionSession, reason: String) {

        val sourceType = StreamUrl.getSourceType(url = session.streamUrl ?: "")

        when (sourceType) {
            StreamSourceType.FLOW -> {
            }
            StreamSourceType.SEED -> {
                baseStreamResolver.reportSeedFailure(session)
            }
            else -> {
                FLog.d(LogCategory.NETWORK, "[Lvl 3] Base Stream playing correctly from: ")
            }
        }

    }

    override suspend fun cacheStreamUrl(
        assetId: String,
        mediaType: MediaType,
        url: String?
    ) {

        if (url == null) {
            streamCacheDao.deleteCache(assetId)
            return
        }

        val existing = streamCacheDao.getCache(assetId)
        if (existing != null) {
            streamCacheDao.updateUrl(assetId, url, System.currentTimeMillis())
        } else {
            streamCacheDao.insertCache(
                StreamCacheEntity(
                    assetId = assetId,
                    cachedUrl = url,
                    cachedDrmKeys = null,
                    timestamp = System.currentTimeMillis(),
                    mediaType = mediaType.value
                )
            )
        }

    }

    override suspend fun getCachedStreamUrl(
        assetId: String,
        mediaType: MediaType
    ): Pair<String?, Long?> {
        val cache = streamCacheDao.getCache(assetId)
        return Pair(cache?.cachedUrl, cache?.timestamp)
    }

}