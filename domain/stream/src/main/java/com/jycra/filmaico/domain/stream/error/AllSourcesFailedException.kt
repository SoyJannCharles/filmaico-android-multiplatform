package com.jycra.filmaico.domain.stream.error

/**
 * Representa un error crítico que ocurre cuando un contenido se queda
 * sin fuentes de video viables después de intentar todas las disponibles.
 *
 * @param contentId El ID del contenido que falló.
 * @param contentType El tipo de contenido (movie, series, etc.).
 * @param sourcesTried El número total de fuentes que se intentaron antes de fallar.
 */
class AllSourcesFailedException(
    val contentId: String,
    val contentType: String,
    val sourcesTried: Int
) : Exception("All sources ($sourcesTried) failed for contentId: $contentId, type: $contentType")