package com.jycra.filmaico.core.player.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.google.gson.Gson
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.core.player.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): ExoPlayer {
        return ExoPlayer
            .Builder(
                context, DefaultRenderersFactory(context)
                    .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                    .setEnableDecoderFallback(true)
            )
            .setTrackSelector(
                DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())
            )
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(30000, 50000, 1000, 1500)
                    .setBackBuffer(10000, true)
                    .setPrioritizeTimeOverSizeThresholds(true)
                    .build()
            )
            .setBandwidthMeter(
                DefaultBandwidthMeter
                    .Builder(context)
                    .build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun providePlayerManager(
        @ApplicationContext context: Context,
        @XAuthHttpClient client: OkHttpClient,
        exoPlayer: ExoPlayer,
        gson: Gson
    ): PlayerManager {
        return PlayerManager(context, client, exoPlayer, gson)
    }

}