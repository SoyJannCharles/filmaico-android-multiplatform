package com.jycra.filmaico.data.stream.di

import com.jycra.filmaico.data.stream.repository.AttrStreamRepositoryImpl
import com.jycra.filmaico.data.stream.repository.StreamProcessingRepositoryImpl
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
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
        streamProcessingRepositoryImpl: StreamProcessingRepositoryImpl
    ): StreamProcessingRepository

    @Binds
    @Singleton
    abstract fun bindAttrStreamRepository(
        attrStreamRepositoryImpl: AttrStreamRepositoryImpl
    ): AttrStreamRepository

}