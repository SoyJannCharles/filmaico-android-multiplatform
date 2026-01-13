package com.jycra.filmaico.core.ui.util.focus

data class ContentFocusCallbacks(

    val onFocusConsumed: () -> Unit,
    val onFocusRestored: () -> Unit

)