package com.jycra.filmaico.domain.media.model.metadata

import com.jycra.filmaico.domain.media.model.Media

data class PlaybackNavigation(
    val parentContainerId: String,
    val nextAsset: Media.Asset?,
    val prevAsset: Media.Asset?
)