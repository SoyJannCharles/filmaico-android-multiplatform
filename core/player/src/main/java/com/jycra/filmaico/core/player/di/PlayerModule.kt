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
import com.jycra.filmaico.core.network.di.PlayerHttpClient
import com.jycra.filmaico.core.player.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object PlayerModule {

    @OptIn(UnstableApi::class)
    @Provides
    @ViewModelScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): ExoPlayer {
        return ExoPlayer
            .Builder(
                context,
                DefaultRenderersFactory(context)
                    .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
                    .setEnableDecoderFallback(true)
            )
            .setTrackSelector(
                DefaultTrackSelector(
                    context,
                    AdaptiveTrackSelection.Factory()
                )
            )
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(30_000, 50_000, 1_000, 2_000)
                    .setBackBuffer(10_000, true)
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
    @ViewModelScoped
    fun providePlayerManager(
        @ApplicationContext context: Context,
        @PlayerHttpClient playerHttpClient: OkHttpClient, // <-- Ingrediente que faltaba,
        gson: Gson, // <-- Ingrediente que faltaba,
        exoPlayer: ExoPlayer
    ): PlayerManager {
        // Ahora le pasamos todos los ingredientes que el constructor necesita
        return PlayerManager(context, playerHttpClient, gson, exoPlayer)
    }

}