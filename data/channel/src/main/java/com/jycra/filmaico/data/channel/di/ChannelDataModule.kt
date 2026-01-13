package com.jycra.filmaico.data.channel.di

import com.jycra.filmaico.data.channel.repository.ChannelRepositoryImpl
import com.jycra.filmaico.data.channel.repository.WatchHistoryRepositoryImpl
import com.jycra.filmaico.domain.channel.repository.ChannelRepository
import com.jycra.filmaico.domain.channel.repository.WatchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChannelDataModule {

    @Binds
    @Singleton
    abstract fun bindChannelRepository(
        impl: ChannelRepositoryImpl
    ): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindWatchHistoryRepository(
        impl: WatchHistoryRepositoryImpl
    ): WatchHistoryRepository

}