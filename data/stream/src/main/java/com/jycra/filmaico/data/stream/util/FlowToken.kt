package com.jycra.filmaico.data.stream.util

import android.util.Base64

object FlowToken {

    fun isValid(url: String?, bufferSeconds: Long = 300L): Boolean {

        if (url.isNullOrBlank() || !url.contains("/tok_")) return false

        return try {

            val currentTimeSeconds = System.currentTimeMillis() / 1000
            val expiration = extractExpiration(url)

            expiration > (currentTimeSeconds + bufferSeconds)

        } catch (e: Exception) {
            false
        }

    }

    fun extractExpiration(url: String): Long {
        return try {

            val jwtPart = url.substringAfter("/tok_", "")
                .substringBefore("/", "")
                .split(".")
                .getOrNull(1) ?: return 0L

            val decodedBytes = Base64.decode(jwtPart, Base64.URL_SAFE or Base64.NO_WRAP)
            val payloadString = String(decodedBytes)

            val pattern = Regex("\"exp\"\\s*:\\s*\"?(\\d+)\"?")
            val match = pattern.find(payloadString)

            match?.groupValues?.get(1)?.toLongOrNull() ?: 0L

        } catch (e: Exception) {
            0L
        }
    }

}