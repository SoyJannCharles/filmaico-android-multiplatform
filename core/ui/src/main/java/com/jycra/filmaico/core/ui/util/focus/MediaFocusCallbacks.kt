package com.jycra.filmaico.core.ui.util.focus

data class MediaFocusCallbacks(

    val onFocusConsumed: () -> Unit,
    val onFocusRestored: () -> Unit,

    val onFocusLeft: (Int, Int) -> Unit = { _, _ -> },
    val onBeaconReceived: () -> Unit = {},

    val onPreloadRequested: (carouselIndex: Int, contentIndex: Int) -> Unit = { _, _ -> }

)