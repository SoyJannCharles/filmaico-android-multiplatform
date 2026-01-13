package com.jycra.filmaico.core.ui.util.focus

data class BrowseFocusState(
    val lastFocusedCarouselIndex: Int? = null,
    val lastFocusedContentIndex: Int? = null,
    val hasConsumedInitialFocus: Boolean = false,
    val shouldRestoreFocus: Boolean = true
)