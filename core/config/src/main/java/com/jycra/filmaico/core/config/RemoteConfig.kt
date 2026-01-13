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
            "DUMMY_IV" to "",
            "DUMMY_KEY_256" to "",
            "DUMMY_AUTH_HEADER" to "",
            "DUMMY_USER_AGENT" to "",
            "https://api.example.com/jwt" to "",
            "https://cdn.example.com/token" to ""
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