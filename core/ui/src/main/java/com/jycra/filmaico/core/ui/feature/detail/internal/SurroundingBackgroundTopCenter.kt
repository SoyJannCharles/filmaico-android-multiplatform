package com.jycra.filmaico.core.ui.feature.detail.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.ui.component.image.BlurredImage
import com.jycra.filmaico.core.ui.theme.color.Gradient

@Composable
fun SurroundingBackgroundTopCenter(
    backgroundUrl: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.24f)
    ) {

        BlurredImage(
            modifier = Modifier
                .fillMaxSize(),
            imageUrl = backgroundUrl
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Gradient.verticalBackgroundGradient())
        )

    }

}