package com.jycra.filmaico.core.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.jycra.filmaico.core.common.AppInitializer
import javax.inject.Inject

class RemoteConfig @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : ConfigInitializer, ConfigSource {

    override fun initialize() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            AppInitializer.remoteConfigReady.complete(task.isSuccessful)
        }
    }

    init {
        firebaseRemoteConfig.setDefaultsAsync(mapOf(
            "app_version_code" to -1,
            "app_version_name" to "0.0.0",
            "aes_decryption_pass_iv" to "",
            "aes_decryption_pass_key" to "",
            "tvar_cdn_auth_header" to "",
            "drm_user_agent" to "",
            "cvattv_jwt_url" to "",
            "cvattv_cdn_token_url" to ""
        ))
    }

    override fun getAppVersionCode(): Long =
        firebaseRemoteConfig.getLong("app_version_code")

    override fun getAppVersionName(): String =
        firebaseRemoteConfig.getString("app_version_name")

    override fun getAesPassIv(): String =
        firebaseRemoteConfig.getString("aes_decryption_pass_iv")

    override fun getAesPassKey(): String =
        firebaseRemoteConfig.getString("aes_decryption_pass_key")

    override fun getTvarCdnAuthHeader(): String =
        firebaseRemoteConfig.getString("tvar_cdn_auth_header")

    override fun getDrmUserAgent(): String =
        firebaseRemoteConfig.getString("drm_user_agent")

    override fun getCvattvJwtUrl(): String =
        firebaseRemoteConfig.getString("cvattv_jwt_url")

    override fun getCvattvCdnTokenUrl(): String =
        firebaseRemoteConfig.getString("cvattv_cdn_token_url")

}