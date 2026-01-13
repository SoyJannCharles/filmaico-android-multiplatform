package com.jycra.filmaico.core.database.di

import android.content.Context
import androidx.room.Room
import com.jycra.filmaico.core.database.FilmaicoDatabase
import com.jycra.filmaico.data.anime.data.dao.AnimeCarouselDao
import com.jycra.filmaico.data.anime.data.dao.AnimeDao
import com.jycra.filmaico.data.channel.data.dao.ChannelCarouselDao
import com.jycra.filmaico.data.channel.data.dao.ChannelDao
import com.jycra.filmaico.data.channel.data.dao.WatchHistoryDao
import com.jycra.filmaico.data.movie.data.dao.MovieCarouselDao
import com.jycra.filmaico.data.movie.data.dao.MovieDao
import com.jycra.filmaico.data.serie.data.dao.SerieCarouselDao
import com.jycra.filmaico.data.serie.data.dao.SerieDao
import com.jycra.filmaico.data.stream.data.dao.DrmKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): FilmaicoDatabase {
        return Room.databaseBuilder(
            context,
            FilmaicoDatabase::class.java,
            "filmaico-db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideMovieCarouselDao(database: FilmaicoDatabase): MovieCarouselDao =
        database.movieCarouselDao()

    @Provides
    @Singleton
    fun provideMovieDao(database: FilmaicoDatabase): MovieDao =
        database.movieDao()

    @Provides
    @Singleton
    fun provideSerieCarouselDao(database: FilmaicoDatabase): SerieCarouselDao =
        database.serieCarouselDao()

    @Provides
    @Singleton
    fun provideSerieDao(database: FilmaicoDatabase): SerieDao =
        database.serieDao()

    @Provides
    @Singleton
    fun provideChannelCarouselDao(database: FilmaicoDatabase): ChannelCarouselDao =
        database.channelCarouselDao()

    @Provides
    @Singleton
    fun provideChannelDao(filmaicoDatabase: FilmaicoDatabase): ChannelDao =
        filmaicoDatabase.channelDao()

    @Provides
    @Singleton
    fun provideWatchHistoryDao(database: FilmaicoDatabase): WatchHistoryDao =
        database.watchHistoryDao()

    @Provides
    @Singleton
    fun provideAnimeCarouselDao(database: FilmaicoDatabase): AnimeCarouselDao =
        database.animeCarouselDao()

    @Provides
    @Singleton
    fun provideAnimeDao(database: FilmaicoDatabase): AnimeDao =
        database.animeDao()

    @Provides
    @Singleton
    fun provideDrmKeyDao(database: FilmaicoDatabase): DrmKeyDao =
        database.drmKeyDao()

}