package com.jycra.filmaico.data.stream.repository

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.security.DecryptionManager
import com.jycra.filmaico.data.stream.data.service.AttrStreamService
import com.jycra.filmaico.data.stream.data.store.CookieStore
import com.jycra.filmaico.data.stream.data.store.JwtStore
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Key
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import kotlin.collections.firstOrNull

class AttrStreamRepositoryImpl @Inject constructor(
    private val configSource: ConfigSource,
    private val attrStreamService: AttrStreamService,
    private val jwtStore: JwtStore,
    private val cookieStore: CookieStore,
    private val decryptionManager: DecryptionManager
) : AttrStreamRepository {

    private val jwtUrl: String by lazy {
        decryptionManager.decrypt(configSource.getCvattvJwtUrl())
    }

    private val cdnTokenUrl: String by lazy {
        decryptionManager.decrypt(configSource.getCvattvCdnTokenUrl())
    }

    override suspend fun getProcessedUri(
        uri: String,
        forceRefresh: Boolean
    ): String = withContext(Dispatchers.IO) {

        val needsCdnToken = uri.contains("cvattv.com.ar", ignoreCase = true)
        if (!needsCdnToken) {
            return@withContext uri
        }

        val jwt = getJwt(forceRefresh)
        val cdnToken = getCdnToken(uri, jwt)

        "$uri?cdntoken=$cdnToken"

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

        val response = attrStreamService.getJwt(jwtUrl)

        if (!response.isSuccessful) {
            throw Exception("Error HTTP JWT principal: ${response.code()}")
        }

        val body = response.body()?.string()
            ?: throw Exception("JWT response body es null")

        val jwtMap = Gson().fromJson(body, Map::class.java)
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

        val response = attrStreamService.getCdnToken(
            url = tokenUrl,
            authorization = "Bearer $jwt",
            origin = "https://portal.app.flow.com.ar",
            referer = "https://portal.app.flow.com.ar/"
        )

        if (!response.isSuccessful) {
            throw Exception("CDN token request failed: ${response.code()}")
        }

        val body = response.body()?.string()
            ?: throw Exception("CDN token response body es null")

        val tokenMap = Gson().fromJson(body, Map::class.java) as? Map<String, Any>
        return tokenMap?.get("token") as? String
            ?: throw Exception("Token no encontrado en respuesta")

    }

    override suspend fun getCookies(
        url: String,
        forceRefresh: Boolean
    ): String? {

        val cachedCookie = cookieStore.getCookie()
        if (cachedCookie != null) {
            return cachedCookie
        }

        return try {

            val cookie = attrStreamService.getCookies(url).firstOrNull() ?: return null
            val cookieString = "${cookie.name}=${cookie.value}"

            cookieStore.saveCookie(cookieString)

            cookieString

        } catch (e: Exception) {
            null
        }

    }

    override suspend fun fetchDrmKeysFromNetwork(url: String): DrmKeys {

        val response = attrStreamService.getDrmKeys(
            url = url,
            userAgent = configSource.getDrmUserAgent(),
            body = "".toRequestBody("application/octet-stream".toMediaType())
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

}