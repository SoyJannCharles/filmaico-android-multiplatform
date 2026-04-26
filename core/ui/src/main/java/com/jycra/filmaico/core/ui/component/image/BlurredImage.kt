package com.jycra.filmaico.core.ui.component.image

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.util.transform.LegacyBlurTransformation

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BlurredImage(
    modifier: Modifier = Modifier,
    zoom: Float = 1.0f,
    blurRadius: Dp = 30.dp,
    imageUrl: String,
    contentDescription: String? = stringResource(R.string.ui_component_image_blurred_content_description)
) {

    val context = LocalContext.current
    val density = LocalDensity.current

    var useLegacyBlur by remember { mutableStateOf(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) }
    var isError by remember(imageUrl) { mutableStateOf(false) }

    Box(modifier = modifier) {

        if (isError) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            )

        } else if (useLegacyBlur) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .transformations(LegacyBlurTransformation(context = context, radius = 18f))
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize().graphicsLayer { scaleX = zoom; scaleY = zoom },
                contentScale = ContentScale.Crop,
                onError = { isError = true }
            )

        } else {

            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        try {
                            val blurPx = with(density) { blurRadius.toPx() }
                            if (blurPx > 0f) {
                                renderEffect = RenderEffect.createBlurEffect(
                                    blurPx, blurPx, Shader.TileMode.MIRROR
                                ).asComposeRenderEffect()
                            }
                            scaleX = zoom
                            scaleY = zoom
                            clip = true
                        } catch (e: Exception) {
                            useLegacyBlur = true
                        }
                    },
                onError = { isError = true }
            )

        }

    }

}