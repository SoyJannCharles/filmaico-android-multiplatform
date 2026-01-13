package com.jycra.filmaico.domain.stream.model

import com.google.gson.annotations.SerializedName

data class DrmInfo(
    val scheme: String = "clearkey",
    val licenseUrl: String
)

data class DrmKeys(
    @SerializedName("keys")
    val keys: List<Key>
)

data class Key(
    @SerializedName("kty")
    val kty: String,
    @SerializedName("k")
    val k: String,
    @SerializedName("kid")
    val kid: String
)