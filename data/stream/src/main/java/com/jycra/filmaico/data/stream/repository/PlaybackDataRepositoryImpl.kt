package com.jycra.filmaico.data.stream.repository

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.security.DecryptionManager
import com.jycra.filmaico.data.stream.data.cache.StreamManifestCache
import com.jycra.filmaico.data.stream.data.dao.StreamCacheDao
import com.jycra.filmaico.data.stream.data.service.StreamService
import com.jycra.filmaico.data.stream.data.store.CookieStore
import com.jycra.filmaico.data.stream.data.store.JwtStore
import com.jycra.filmaico.data.stream.entity.StreamCacheEntity
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Key
import com.jycra.filmaico.domain.stream.repository.EdgeNodeRepository
import com.jycra.filmaico.domain.stream.repository.PlaybackDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import com.jycra.filmaico.data.stream.util.FlowUrlResolver

@Singleton
class PlaybackDataRepositoryImpl @Inject constructor(
    private val configSource: ConfigSource,
    private val decryptionManager: DecryptionManager,
    private val edgeNodeRepository: EdgeNodeRepository,
    private val flowUrlResolver: FlowUrlResolver,
    private val streamService: StreamService,
    private val streamCacheDao: StreamCacheDao,
    private val streamManifestCache: StreamManifestCache,
    private val jwtStore: JwtStore,
    private val cookieStore: CookieStore,
) : PlaybackDataRepository {

    private val jwtUrl: String by lazy {
        decryptionManager.decrypt(configSource.getCvattvJwtUrl())
    }

    private val cdnTokenUrl: String by lazy {
        decryptionManager.decrypt(configSource.getCvattvCdnTokenUrl())
    }

    override suspend fun resolveUrlViaWebView(iframeUrl: String): Flow<String> {
        return streamService.resolveUrlFromWebView(iframeUrl)
    }

    override suspend fun getAuthenticatedUri(
        uri: String,
        forceRefresh: Boolean
    ): String = withContext(Dispatchers.IO) {

        val needsCdnToken = uri.contains("cvattv.com.ar", ignoreCase = true)
        if (!needsCdnToken) {
            return@withContext uri
        }

        val cachedOrPoolUrl = edgeNodeRepository.getOptimalEdgeHost(uri)

        if (cachedOrPoolUrl != uri && cachedOrPoolUrl.contains("/tok_")) {
            return@withContext cachedOrPoolUrl
        }


        val jwt = getJwt(forceRefresh)
        val cdnToken = getCdnToken(uri, jwt)

        val urlWithTokenParam = "$uri?cdntoken=$cdnToken"

        val defaultUa = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36"
        val extraHeaders = mapOf(
            "Origin" to "https://portal.app.flow.com.ar",
            "Referer" to "https://portal.app.flow.com.ar/"
        )

        // --- FASE 3: LA RESOLUCIÓN ---
        // Nos disfrazamos de PC, seguimos el salto 302 y obtenemos la URL dorada
        val finalResolvedUrl = flowUrlResolver.resolve(urlWithTokenParam, defaultUa, extraHeaders)

        // --- FASE 4: SEMBRAR PARA EL FUTURO ---
        // Si logramos resolver una URL nueva y exitosa, le avisamos al Repositorio.
        if (finalResolvedUrl != urlWithTokenParam && finalResolvedUrl.contains("/tok_")) {
            // Esta función (que tendrías que agregar a tu EdgeNodeRepository) guardaría
            // la URL en tu caché local y llamaría al publishAsync del PHP.
            //edgeNodeRepository.reportSuccessAndPublish(uri, finalResolvedUrl)
        }

        // Le entregamos el resultado final a ExoPlayer
        return@withContext finalResolvedUrl

    }

    private suspend fun getJwt(forceRefresh: Boolean): String {

        val currentTimeSec = System.currentTimeMillis() / 1000

        if (!forceRefresh) {

            val cachedJwt = jwtStore.getJwt()
            val cachedExp = jwtStore.getJwtExp()

            if (cachedJwt != null && cachedExp > currentTimeSec + 60L) {
                return cachedJwt
            }

        }

        val token = streamService.fetchJwtToken(jwtUrl)

        val jwtMap = Gson().fromJson(token, Map::class.java)
        val jwt = jwtMap?.get("jwt") as? String
            ?: throw Exception("JWT no encontrado en respuesta")

        val exp = extractExpFromJwt(jwt)

        jwtStore.saveJwt(jwt, exp)

        return jwt

    }

    private fun extractExpFromJwt(jwt: String): Long {

        try {

            val parts = jwt.split(".")
            if (parts.size < 2) {
                throw Exception("JWT inválido: formato incorrecto")
            }

            val payloadJson = String(
                android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE),
                Charsets.UTF_8
            )

            val payload = Gson().fromJson(payloadJson, Map::class.java) as? Map<String, Any>
            val exp = payload?.get("exp")

            return when (exp) {
                is Double -> exp.toLong()
                is Number -> exp.toLong()
                else -> 0L
            }

        } catch (e: Exception) {
            return 0L
        }

    }

    private suspend fun getCdnToken(videoUrl: String, jwt: String): String {

        val encodedUrl = Uri.encode(videoUrl)
        val tokenUrl = "$cdnTokenUrl$encodedUrl"

        val token = streamService.fetchCdnToken(
            url = tokenUrl,
            authorization = "Bearer $jwt",
            origin = "https://portal.app.flow.com.ar",
            referer = "https://portal.app.flow.com.ar/"
        )

        val tokenMap = Gson().fromJson(token, Map::class.java) as? Map<String, Any>
        return tokenMap?.get("token") as? String
            ?: throw Exception("Token no encontrado en respuesta")

    }

    override suspend fun getCookies(
        url: String,
        forceRefresh: Boolean
    ): String? {

        val currentTimeSec = System.currentTimeMillis() / 1000

        if (!forceRefresh) {
            val cachedCookie = cookieStore.getCookie()
            if (cachedCookie != null) {
                val exp = extractExpFromCookie(cachedCookie)
                if (exp > currentTimeSec + 60L) {
                    return cachedCookie
                }
            }
        }

        return try {

            val cookie = streamService.fetchCookies(url).firstOrNull() ?: return null
            val cookieString = "${cookie.name}=${cookie.value}"

            cookieStore.saveCookie(cookieString)

            cookieString

        } catch (e: Exception) {
            null
        }

    }

    private fun extractExpFromCookie(cookie: String): Long {
        return try {
            val match = Regex("exp=(\\d+)").find(cookie)
            match?.groupValues?.get(1)?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    override suspend fun fetchDrmKeys(url: String): DrmKeys {

        val response = streamService.fetchDrmKeys(
            url = url,
            userAgent = configSource.getDrmUserAgent(),
            payload = ""
        )

        val decryptedKeys = response.keys.map { networkKey ->
            val decryptedK = decryptionManager.decrypt(networkKey.k)
            val decryptedKid = decryptionManager.decrypt(networkKey.kid)
            Key(
                kty = networkKey.kty,
                k = decryptedK,
                kid = decryptedKid
            )
        }

        return DrmKeys(keys = decryptedKeys)

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

    override suspend fun preloadHlsManifest(url: String) {
        streamService.fetchHlsManifest(url)
            .catch { e ->
                Log.e("StreamPreloadManager", "Error fetching HLS manifest: ${e.message}")
            }
            .collect { (requestedUrl, finalUrl, content) ->
                streamManifestCache.put(
                    requestedUrl = requestedUrl,
                    finalUrl = finalUrl,
                    content = content
                )
            }
    }

    override suspend fun getManifestContent(url: String): String? {
        return try {
            val result = streamService.fetchHlsManifest(url, includeChildren = false).first()
            result.third
        } catch (e: Exception) {
            Log.e("StreamRepository", "Error obteniendo master para análisis: ${e.message}")
            null
        }
    }

}