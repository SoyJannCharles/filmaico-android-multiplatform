package com.jycra.filmaico.core.player.di

import com.jycra.filmaico.core.player.RamManifestCache
import com.jycra.filmaico.data.stream.data.cache.StreamManifestCache
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManifestModule {

    @Binds
    @Singleton
    abstract fun bindStreamManifestCache(
        impl: RamManifestCache
    ): StreamManifestCache

}