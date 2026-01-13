package com.jycra.filmaico.data.stream.mapper

import com.jycra.filmaico.core.model.stream.DrmKeysDto
import com.jycra.filmaico.core.model.stream.KeyDto
import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.model.Key

fun DrmKeys.toDto(): DrmKeysDto {
    return DrmKeysDto(
        keys = this.keys.map {
            KeyDto(kty = it.kty, k = it.k, kid = it.kid)
        }
    )
}

fun DrmKeysDto.toDomain(): DrmKeys {
    return DrmKeys(
        keys = this.keys?.map {
            Key(kty = it.kty ?: "", k = it.k ?: "", kid = it.kid ?: "")
        } ?: emptyList()
    )
}