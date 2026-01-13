package com.jycra.filmaico.core.ui.component.image

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@Composable
fun BlurredImage(
    modifier: Modifier = Modifier,
    zoom: Float = 1.0f,
    blurRadius: Dp = 30.dp,
    imageUrl: String,
    contentDescription: String? = stringResource(R.string.ui_component_image_blurred_content_description)
) {

    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        val density = LocalDensity.current

        val blurEffect = remember(blurRadius) {

            val blurRadiusPx = with(density) { blurRadius.toPx() }

            RenderEffect.createBlurEffect(
                blurRadiusPx,
                blurRadiusPx,
                Shader.TileMode.MIRROR
            ).asComposeRenderEffect()

        }

        AsyncImage(
            modifier = modifier
                .graphicsLayer {
                    scaleX = zoom
                    scaleY = zoom
                    renderEffect = blurEffect
                    clip = true
                },
            contentScale = ContentScale.Crop,
            model = imageUrl,
            contentDescription = contentDescription
        )

    } else {

        val imageRequest = ImageRequest.Builder(context)
            .data(imageUrl)
            .transformations(
                LegacyBlurTransformation(
                    context = context,
                    radius = 16f,
                    sampling = 6,
                )
        )
        .build()

        AsyncImage(
            modifier = modifier
                .scale(zoom),
            contentScale = ContentScale.Crop,
            model = imageRequest,
            contentDescription = contentDescription
        )

    }

}