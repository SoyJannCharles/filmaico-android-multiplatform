package com.jycra.filmaico.domain.stream.model.metadata

import java.util.Locale

data class AudioMetadata(
    val code: String,
    val subtitleCode: String? = null
) {

    val displayAudio: String
        get() = getDisplayLanguage(code)

    val displaySubtitle: String?
        get() = if (subtitleCode != null) "Sub. ${getDisplayLanguage(subtitleCode)}" else null

    fun getDisplayLanguage(code: String): String {
        return try {

            val cleanCode = code.replace("-r", "-")

            val locale = Locale.forLanguageTag(cleanCode)
            locale.getDisplayLanguage(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                .ifBlank { code }

        } catch (e: Exception) {
            code
        }
    }

}