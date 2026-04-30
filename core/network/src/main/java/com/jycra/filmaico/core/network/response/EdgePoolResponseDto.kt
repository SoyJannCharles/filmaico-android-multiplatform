package com.jycra.filmaico.core.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EdgePoolResponseDto(
    @SerializedName("edges") val edges: List<String>?,
    @SerializedName("edge") val legacyEdge: String?
) {

    fun toHostList(): List<String> {
        return edges?.filter { it.isNotBlank() && it.lowercase() != "null" }
            ?: legacyEdge?.takeIf { it.isNotBlank() && it.lowercase() != "null" }?.let { listOf(it) }
            ?: emptyList()
    }

}