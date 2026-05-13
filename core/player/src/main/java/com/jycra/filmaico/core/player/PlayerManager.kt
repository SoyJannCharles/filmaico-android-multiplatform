package com.jycra.filmaico.core.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
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
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.model.PlaybackParams
import com.jycra.filmaico.domain.stream.util.PlayerErrorType
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @XAuthHttpClient private val xAuthClient: OkHttpClient,
    val exoPlayer: ExoPlayer,
    val gson: Gson
) {

    private var onErrorCallback: ((error: PlaybackException) -> Unit)? = null

    fun setPlaybackErrorCallback(callback: (error: PlaybackException) -> Unit) {
        this.onErrorCallback = callback
    }

    private var preparedUri: String? = null

    fun isPreparedFor(uri: String): Boolean = preparedUri == uri

    @OptIn(UnstableApi::class)
    fun prepareInBackground(playbackParams: PlaybackParams) {

        if (preparedUri == playbackParams.uri) {
            FLog.d(LogCategory.PLAYER, "Lvl 2: Already prepared or buffering: ${playbackParams.uri}")
            return
        }

        Handler(Looper.getMainLooper()).post {
            try {

                FLog.d(LogCategory.PLAYER, "Lvl 2: Initializing Hardware & Buffer for ${playbackParams.uri}")

                val mediaSource = provideMediaSource(playbackParams)

                exoPlayer.stop()
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()

                preparedUri = playbackParams.uri

            } catch (e: Exception) {
                FLog.e(LogCategory.PLAYER, "Lvl 2: Preparation failed", e)
                preparedUri = null
            }
        }

    }

    @OptIn(UnstableApi::class)
    fun playStream(playbackParams: PlaybackParams) {
        // 1. Preparamos el buffer en el hilo principal (ExoPlayer requiere MainThread)
        Handler(Looper.getMainLooper()).post {
            try {
                val mediaSource = provideMediaSource(playbackParams)

                exoPlayer.stop()
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()

                // 2. Le damos a Play inmediatamente
                exoPlayer.playWhenReady = true
                preparedUri = playbackParams.uri

                FLog.d(LogCategory.PLAYER, "Lvl 3: Stream prepared and playing: ${playbackParams.uri}")
            } catch (e: Exception) {
                FLog.e(LogCategory.PLAYER, "Lvl 3: Failed to play stream", e)
                preparedUri = null
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun provideMediaSource(playbackParams: PlaybackParams): MediaSource {

        val dataSourceFactory = OkHttpDataSource.Factory(xAuthClient)
            .setDefaultRequestProperties(playbackParams.headers ?: emptyMap())

        val mediaItem = MediaItem.Builder()
            .setUri(playbackParams.uri)
            .setMimeType(inferMimeType(playbackParams.uri))
            .build()

        val urlLower = playbackParams.uri.lowercase()

        return if (urlLower.contains(".mp4") || urlLower.contains(".mkv")) {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
        } else {
            DefaultMediaSourceFactory(context)
                .setDataSourceFactory(dataSourceFactory)
                .setDrmSessionManagerProvider(provideDrmManagerProvider(playbackParams.keys))
                .createMediaSource(mediaItem)
        }

    }

    private fun inferMimeType(uri: String): String {
        val url = uri.lowercase().substringBefore("?")
        return when {
            url.endsWith(".m3u8") -> MimeTypes.APPLICATION_M3U8
            url.endsWith(".mpd") -> MimeTypes.APPLICATION_MPD
            url.endsWith(".mp4") -> MimeTypes.VIDEO_MP4
            else -> MimeTypes.APPLICATION_M3U8
        }
    }

    @OptIn(UnstableApi::class)
    private fun provideDrmManagerProvider(keys: DrmKeys?): DrmSessionManagerProvider {

        if (keys == null) return DrmSessionManagerProvider { DrmSessionManager.DRM_UNSUPPORTED }

        FLog.d(LogCategory.PLAYER, "Lvl 2: Injecting ClearKey DRM Session")

        val drmCallback = LocalMediaDrmCallback(gson.toJson(keys).toByteArray(StandardCharsets.UTF_8))

        return DrmSessionManagerProvider {
            DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(C.CLEARKEY_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                .build(drmCallback)
        }

    }

    fun play() {
        if (exoPlayer.playbackState != Player.STATE_IDLE) {
            FLog.d(LogCategory.PLAYER, "Lvl 3: User pressed Play. Buffer status: ${exoPlayer.bufferedPercentage}%")
            exoPlayer.playWhenReady = true
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

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun currentPosition(): Long = exoPlayer.currentPosition

    fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    fun setupListeners(
        onIsPlayingChanged: (isPlaying: Boolean) -> Unit,
        onPlaybackStateChanged: (state: Int) -> Unit,
        onPlayerError: (type: PlayerErrorType, retry: () -> Unit, seekToDefault: () -> Unit, nextSource: () -> Unit) -> Unit

    ) {

        exoPlayer.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                onIsPlayingChanged(isPlaying)
            }

            override fun onPlaybackStateChanged(state: Int) {
                FLog.d(LogCategory.PLAYER, "Player State: $state")
                onPlaybackStateChanged(state)
            }

            override fun onPlayerError(error: PlaybackException) {

                val type = mapError(error)

                onPlayerError(
                    type,
                    { exoPlayer.prepare(); exoPlayer.play() }, // retry
                    { exoPlayer.seekToDefaultPosition(); exoPlayer.prepare() }, // seekToDefault
                    { /* Acción para saltar a otra fuente si fuera necesario aquí */ }
                )

            }

        })

    }

    private fun mapError(error: PlaybackException): PlayerErrorType =
        when (error.errorCode) {

            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> PlayerErrorType.BehindLiveWindow
            PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE -> PlayerErrorType.ReadPosition

            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> PlayerErrorType.NetworkInstability

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
            PlaybackException.ERROR_CODE_DECODING_FAILED -> PlayerErrorType.Decoding

            PlaybackException.ERROR_CODE_IO_UNSPECIFIED -> PlayerErrorType.EdgeDisconnection

            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
            PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED -> PlayerErrorType.SourceFailed

            else -> PlayerErrorType.Unknown(error.errorCode)

        }

}