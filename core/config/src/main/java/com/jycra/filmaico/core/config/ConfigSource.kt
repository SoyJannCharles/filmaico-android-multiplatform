package com.jycra.filmaico.core.config

interface ConfigSource {

    fun getAppVersionCode(): Long
    fun getAppVersionName(): String

    fun getAesPassIv(): String
    fun getAesPassKey(): String

    fun getTvarCdnAuthHeader(): String
    fun getDrmUserAgent(): String

    fun getCvattvJwtUrl(): String
    fun getCvattvCdnTokenUrl(): String

}