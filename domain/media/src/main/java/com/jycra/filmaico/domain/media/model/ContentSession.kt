package com.jycra.filmaico.domain.media.model

import com.jycra.filmaico.domain.media.model.metadata.PlayerMetadata

data class ContentSession(
    val id: String,
    val type: String,
    val seasonId: String? = null,
    val parentId: String? = null,
    val metadata: PlayerMetadata? = null,
    val currentSourceIndex: Int = 0,
    val lastImageUrl: String = ""
)