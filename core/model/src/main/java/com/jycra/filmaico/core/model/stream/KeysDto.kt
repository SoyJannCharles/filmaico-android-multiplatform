package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class KeyDto(
    @SerializedName("kty")
    val kty: String = "oct",
    @SerializedName("k")
    val k: String = "",
    @SerializedName("kid")
    val kid: String = ""
) {

    fun isValid(): Boolean {
        return k.isNotBlank() && kid.isNotBlank()
    }

}

@Keep
data class KeysDto(
    @SerializedName("keys")
    val keys: List<KeyDto>
)