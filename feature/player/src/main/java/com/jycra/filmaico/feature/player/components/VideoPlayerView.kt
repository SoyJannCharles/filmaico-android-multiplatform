package com.jycra.filmaico.feature.player.components

import android.view.TextureView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun VideoPlayerView(
    exoPlayer: ExoPlayer,
    onViewReady: (playerView: TextureView) -> Unit
) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            TextureView(ctx).apply {
                exoPlayer.setVideoTextureView(this)
                onViewReady(this)
            }
        }
    )

}