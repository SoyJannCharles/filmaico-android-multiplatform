package com.jycra.filmaico.data.stream.di

import com.jycra.filmaico.data.stream.data.provider.internal.DefaultStreamCredentialsProvider
import com.jycra.filmaico.data.stream.data.provider.internal.StreamCredentialsProvider
import com.jycra.filmaico.data.stream.data.source.FirestoreStreamDataSource
import com.jycra.filmaico.data.stream.data.source.StreamDataSource
import com.jycra.filmaico.data.stream.resolver.DefaultFlowStreamResolver
import com.jycra.filmaico.data.stream.repository.StreamRepositoryImpl
import com.jycra.filmaico.data.stream.resolver.DefaultBaseStreamResolver
import com.jycra.filmaico.domain.stream.resolver.FlowStreamResolver
import com.jycra.filmaico.domain.stream.repository.StreamRepository
import com.jycra.filmaico.domain.stream.resolver.BaseStreamResolver
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
    abstract fun bindStreamRepository(
        impl: StreamRepositoryImpl
    ): StreamRepository

    @Binds
    @Singleton
    abstract fun bindFlowStreamResolver(
        impl: DefaultFlowStreamResolver
    ): FlowStreamResolver

    @Binds
    @Singleton
    abstract fun bindBaseStreamResolver(
        impl: DefaultBaseStreamResolver
    ): BaseStreamResolver

    @Binds
    @Singleton
    abstract fun bindStreamDataSource(
        impl: FirestoreStreamDataSource
    ): StreamDataSource

    @Binds
    @Singleton
    abstract fun bindStreamCredentialsProvider(
        impl: DefaultStreamCredentialsProvider
    ): StreamCredentialsProvider

}