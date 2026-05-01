package com.jycra.filmaico.core.firebase.model.stream

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class DrmInfoDto(
    val scheme: String = "clearkey",
    val licenseUrl: String = "",
    val staticKeys: KeyDto = KeyDto()
) {

    fun isValid(): Boolean {
        return licenseUrl.isNotBlank() || staticKeys.isValid()
    }

}