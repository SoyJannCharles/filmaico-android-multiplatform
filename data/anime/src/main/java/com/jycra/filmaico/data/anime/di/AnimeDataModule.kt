package com.jycra.filmaico.data.anime.di

import com.jycra.filmaico.data.anime.repository.AnimeRepositoryImpl
import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnimeDataModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository

}