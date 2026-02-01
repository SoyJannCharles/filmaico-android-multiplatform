package com.jycra.filmaico.data.history.di

import com.jycra.filmaico.data.history.repository.MediaProgressRepositoryImpl
import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryDataModule {

    @Binds
    @Singleton
    abstract fun bindWatchHistoryRepository(
        impl: MediaProgressRepositoryImpl
    ): MediaProgressRepository

}