package com.jycra.filmaico.core.config.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.jycra.filmaico.core.config.ConfigInitializer
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.config.RemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindConfigInitializer(
        remoteConfig: RemoteConfig
    ): ConfigInitializer

    @Binds
    @Singleton
    abstract fun bindConfigSource(
        remoteConfig: RemoteConfig
    ): ConfigSource

    companion object {
        @Provides
        @Singleton
        fun provideRemoteConfig(): FirebaseRemoteConfig {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // Cachear por 1 hora en producción
            })
            return remoteConfig
        }
    }

}