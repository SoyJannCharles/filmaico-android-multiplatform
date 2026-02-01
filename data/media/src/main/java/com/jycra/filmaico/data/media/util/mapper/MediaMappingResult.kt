package com.jycra.filmaico.data.media.util.mapper

import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef

data class MediaMappingResult(
    val media: List<MediaEntity> = emptyList(),
    val tags: List<MediaTagCrossRef> = emptyList()
)