package com.jycra.filmaico.data.serie.di

import com.jycra.filmaico.data.serie.repository.SerieRepositoryImpl
import com.jycra.filmaico.domain.serie.repository.SerieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SerieDataModule {

    @Binds
    @Singleton
    abstract fun bindSerieRepository(
        serieRepositoryImpl: SerieRepositoryImpl
    ): SerieRepository

}