package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep

@Keep
data class DrmInfoDto(
    val scheme: String = "clearkey",
    val licenseUrl: String = "",
    val staticKeys: KeyDto = KeyDto()
) {

    fun isValid(): Boolean {
        return licenseUrl.isNotBlank() || staticKeys.isValid()
    }

}