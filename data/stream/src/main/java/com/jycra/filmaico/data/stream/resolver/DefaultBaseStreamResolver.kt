package com.jycra.filmaico.data.stream.resolver

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.data.stream.data.source.StreamDataSource
import com.jycra.filmaico.data.stream.util.StreamUrl
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import com.jycra.filmaico.data.stream.util.prober.SeedProber
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.resolver.BaseStreamResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultBaseStreamResolver @Inject constructor(
    private val source: StreamDataSource,
    private val iframeResolver: IframeResolver,
    private val prober: SeedProber
) : BaseStreamResolver {

    private val sessionCache = ConcurrentHashMap<String, ResolutionSession>()

    override suspend fun resolve(url: String): ResolutionSession? = withContext(Dispatchers.IO) {

        val provider = StreamUrl.getProviderName(url) ?: return@withContext null
        val videoId = StreamUrl.getVideoId(url) ?: return@withContext null

        val originalDomain = url.toHostOnly() ?: ""

        sessionCache[videoId]?.let { session ->
            FLog.d(LogCategory.SCRAPER, "Lvl 1 HIT: Using session cache for seed $videoId")
            return@withContext session
        }

        val seeds = source.getSeedsByProvider(provider)
        val urlsToProbe = seeds.map { "https://${it.domain}/e/$videoId" }.toMutableList()

        if (urlsToProbe.none { it.contains(originalDomain) }) {
            urlsToProbe.add(url)
        }

        val winningProbe = prober.findWinningSeed(urlsToProbe)
        if (winningProbe != null) {

            val m3u8 = iframeResolver.resolveStatic(winningProbe.url, winningProbe.body)
            if (m3u8 != null) {

                FLog.d(LogCategory.SCRAPER, "Lvl 2 HIT: Seed resolved via static extraction")
                return@withContext newSession(
                    videoId = videoId,
                    url = url,
                    uri = m3u8,
                    domain = winningProbe.url.toHostOnly() ?: originalDomain
                )

            }

        }

        FLog.d(LogCategory.SCRAPER, "Static failed. Launching WebView for $videoId")
        val finalM3u8 = iframeResolver.resolveWithWebView(url).firstOrNull()

        return@withContext finalM3u8?.let { m3u8 ->
            val discoveredDomain = iframeResolver.getLastResolvedDomain() ?: originalDomain
            newSession(
                videoId = videoId,
                url = url,
                uri = m3u8,
                domain = discoveredDomain
            )
        }

    }

    override suspend fun resolveWithWebView(url: String): ResolutionSession? {

        val videoId = StreamUrl.getVideoId(url) ?: "unknown"
        val provider = StreamUrl.getProviderName(url) ?: "generic"

        FLog.d(LogCategory.SCRAPER, "Direct Sniffing for $provider (ID: $videoId)")

        val m3u8 = iframeResolver.resolveWithWebView(url).firstOrNull()

        if (m3u8 != null)
            return null

        return ResolutionSession(
            streamUrl = url,
            host = url.toHostOnly() ?: "",
            resolvedUrl = m3u8!!
        )

    }

    private fun newSession(videoId: String, url: String, uri: String, domain: String): ResolutionSession {

        val session = ResolutionSession(
            streamUrl = url,
            host = domain,
            resolvedUrl = uri
        )

        sessionCache[videoId] = session
        return session

    }

    override suspend fun reportSeedSuccess(session: ResolutionSession) {

        val domain = session.host
        val provider = session.provider ?: ""

        if (domain == null) return

        if (domain.isBlank() || domain.length < 4) return
        if (domain.contains("localhost") || domain.contains("127.0.0.1")) return

        source.saveSeed(domain, provider)

    }

    override suspend fun reportSeedFailure(session: ResolutionSession) {

        val domain = session.host
        val provider = session.provider ?: ""

        if (domain == null) return

        FLog.w(LogCategory.DATABASE, "Reporting failure for $domain (Provider: ${provider})")

        val entry = sessionCache.entries.find { it.value.host == domain }
        entry?.let { sessionCache.remove(it.key) }

        source.deleteSeed(domain, provider)

    }

}