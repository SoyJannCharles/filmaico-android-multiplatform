package com.jycra.filmaico.core.ui.util.focus

import androidx.compose.ui.focus.FocusRequester

data class ContentFocusState(

    val lastFocusedContentIndex: Int? = null,
    val focusArray: MutableList<FocusRequester> = mutableListOf(),
    val hasConsumedInitialFocus: Boolean = false,
    val shouldRestoreFocus: Boolean = true

)