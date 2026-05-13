package com.jycra.filmaico.data.stream.data.provider.internal

import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.security.DecryptionManager
import com.jycra.filmaico.data.stream.data.provider.CookieProvider
import com.jycra.filmaico.data.stream.data.provider.DrmKeyProvider
import com.jycra.filmaico.data.stream.data.store.CookieStore
import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.model.Key
import javax.inject.Inject

class DefaultStreamCredentialsProvider @Inject constructor(
    private val configSource: ConfigSource,
    private val cookieProvider: CookieProvider,
    private val cookieStore: CookieStore,
    private val drmKeyProvider: DrmKeyProvider,
    private val decryptionManager: DecryptionManager
) : StreamCredentialsProvider {

    override suspend fun getCookies(url: String, forceRefresh: Boolean): String? {

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

            val cookie = cookieProvider.fetchCookies(url).firstOrNull() ?: return null
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

    override suspend fun getDrmKeys(url: String): DrmKeys {

        val response = drmKeyProvider.fetchDrmKeys(
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

}