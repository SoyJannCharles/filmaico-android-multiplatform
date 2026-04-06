package com.jycra.filmaico.data.stream.di

import com.jycra.filmaico.data.stream.repository.PlaybackDataRepositoryImpl
import com.jycra.filmaico.domain.stream.repository.PlaybackDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StreamDataModule {

    @Binds
    @Singleton
    abstract fun bindStreamProcessingRepository(
        streamProcessingRepositoryImpl: PlaybackDataRepositoryImpl
    ): PlaybackDataRepository

}