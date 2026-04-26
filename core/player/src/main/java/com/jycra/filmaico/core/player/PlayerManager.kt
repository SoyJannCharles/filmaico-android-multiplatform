package com.jycra.filmaico.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.LocalMediaDrmCallback
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.google.gson.Gson
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.domain.media.model.metadata.PlaybackData
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@ViewModelScoped
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @XAuthHttpClient private val xAuthClient: OkHttpClient,
    private val ramManifestCache: RamManifestCache,
    private val gson: Gson,
    val exoPlayer: ExoPlayer
) {

    private var onErrorCallback: ((error: PlaybackException) -> Unit)? = null

    fun setPlaybackErrorCallback(callback: (error: PlaybackException) -> Unit) {
        this.onErrorCallback = callback
    }

    private val playerListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            onErrorCallback?.invoke(error)
        }
    }

    init {
        exoPlayer.addListener(playerListener)
        exoPlayer.playWhenReady = true
    }

    @OptIn(UnstableApi::class)
    fun createMediaSource(playbackData: PlaybackData): MediaSource {

        val baseHttpDataSourceFactory = OkHttpDataSource.Factory(xAuthClient)
            .setDefaultRequestProperties(playbackData.headers ?: emptyMap())

        val cachingDataSourceFactory = DataSource.Factory {
            RamCacheDataSource(
                upstream = baseHttpDataSourceFactory.createDataSource(),
                cache = ramManifestCache
            )
        }

        val mediaItemBuilder = MediaItem.Builder()
            .setUri(playbackData.uri)

        val urlWithoutParams = playbackData.uri.substringBefore('?').lowercase()

        val isProgressive = urlWithoutParams.endsWith(".mp4") ||
                urlWithoutParams.endsWith(".mkv")

        if (!isProgressive) {

            val isStandardStream = urlWithoutParams.endsWith(".m3u8") ||
                    urlWithoutParams.endsWith(".mpd")

            if (!isStandardStream)
                mediaItemBuilder.setMimeType(MimeTypes.APPLICATION_M3U8)

        }

        val mediaItem = mediaItemBuilder.build()

        Log.e("DRMKeys", "${playbackData.keys}")

        return if (isProgressive) {
            ProgressiveMediaSource.Factory(baseHttpDataSourceFactory)
                .createMediaSource(mediaItem)
        } else {
            DefaultMediaSourceFactory(context)
                .setDataSourceFactory(cachingDataSourceFactory)
                .setDrmSessionManagerProvider(createDrmManagerProvider(keys = playbackData.keys))
                .createMediaSource(mediaItem)
        }

    }

    @OptIn(UnstableApi::class)
    private fun createDrmManagerProvider(keys: DrmKeys?): DrmSessionManagerProvider {

        if (keys == null)
            return DrmSessionManagerProvider { DrmSessionManager.DRM_UNSUPPORTED }

        val drmCallback =
            LocalMediaDrmCallback(gson.toJson(keys).toByteArray(StandardCharsets.UTF_8))

        return DrmSessionManagerProvider {
            DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(C.CLEARKEY_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                .build(drmCallback)
        }

    }

    fun resume() {
        if (exoPlayer.isCurrentMediaItemLive) {
            exoPlayer.seekToDefaultPosition()
        }
        exoPlayer.playWhenReady = true
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun releasePlayer() {
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

}