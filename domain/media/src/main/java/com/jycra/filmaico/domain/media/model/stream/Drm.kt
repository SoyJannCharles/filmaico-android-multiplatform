package com.jycra.filmaico.domain.media.model.stream

import com.google.gson.annotations.SerializedName

data class DrmInfo(
    val scheme: String,
    val licenseUrl: String,
    val staticKeys: Key
) {

    fun isValid(): Boolean {
        return licenseUrl.isNotBlank() || staticKeys.isValid()
    }

}

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
) {

    fun isValid(): Boolean {
        return k.isNotBlank() && kid.isNotBlank()
    }

}