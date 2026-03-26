package com.jycra.filmaico.feature.player.components

import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerView(
    exoPlayer: ExoPlayer,
    onViewReady: (playerView: TextureView) -> Unit
) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->

            val container = AspectRatioFrameLayout(ctx).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            exoPlayer.addListener(object : Player.Listener {
                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    if (videoSize.width > 0 && videoSize.height > 0) {
                        container.setAspectRatio(videoSize.width.toFloat() / videoSize.height.toFloat())
                    }
                }
            })

            container.addView(
                TextureView(ctx).apply {
                    exoPlayer.setVideoTextureView(this)
                    onViewReady(this)
                }
            )

            container

        }
    )

}