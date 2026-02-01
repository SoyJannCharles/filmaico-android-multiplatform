package com.jycra.filmaico.core.ui.util.focus

data class MediaFocusState(
    val lastFocusedCarouselId: String? = null,
    val lastFocusedCarouselIndex: Int = 0,
    val lastFocusedContentIndex: Int = 0,
    val hasConsumedInitialFocus: Boolean = false,
    val shouldRestoreFocus: Boolean = true
)