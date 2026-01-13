package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DrmKeysDto(
    @SerializedName("keys")
    val keys: List<KeyDto>
)

@Keep
data class KeyDto(
    @SerializedName("kty")
    val kty: String,
    @SerializedName("k")
    val k: String,
    @SerializedName("kid")
    val kid: String
)