package com.jycra.filmaico.core.security

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64
import com.jycra.filmaico.core.config.ConfigSource

@Singleton
class DecryptionManager @Inject constructor(
    private val configSource: ConfigSource
) {

    private companion object {
        const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        const val KEY_ALGORITHM = "AES"
        const val HASH_ALGORITHM_MD5 = "MD5"
        const val HASH_ALGORITHM_SHA256 = "SHA-256"
    }

    private data class AesCredentials(val keySpec: SecretKeySpec, val ivSpec: IvParameterSpec)

    private val credentials: AesCredentials by lazy {
        generateAesCredentials()
    }

    fun decrypt(encryptedBase64Text: String): String {
        return try {
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, credentials.keySpec, credentials.ivSpec)
            val encryptedData = Base64.decode(encryptedBase64Text, Base64.DEFAULT)
            val decryptedData = cipher.doFinal(encryptedData)
            String(decryptedData, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

    private fun generateAesCredentials(): AesCredentials {
        return AesCredentials(
            keySpec = SecretKeySpec(
                configSource.getAesPassKey().toHashByteArray(HASH_ALGORITHM_SHA256),
                KEY_ALGORITHM
            ),
            ivSpec = IvParameterSpec(
                configSource.getAesPassIv().toHashByteArray(HASH_ALGORITHM_MD5)
            )
        )
    }

    private fun String.toHashByteArray(algorithm: String): ByteArray {
        return MessageDigest.getInstance(algorithm).digest(this.toByteArray(Charsets.UTF_8))
    }

}