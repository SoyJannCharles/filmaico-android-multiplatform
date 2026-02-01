package com.jycra.filmaico.domain.media.model.metadata

data class PlaybackSession(
    val metadata: PlayerMetadata? = null,
    val nextMediaId: String? = null,
    val prevMediaId: String? = null
)