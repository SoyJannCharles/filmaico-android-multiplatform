package com.jycra.filmaico.domain.stream.model

data class ResolutionResult(
    val resolutionSession: ResolutionSession? = null,
    val playbackParams: PlaybackParams
)