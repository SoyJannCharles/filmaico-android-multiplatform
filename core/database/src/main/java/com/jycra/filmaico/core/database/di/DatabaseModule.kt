package com.jycra.filmaico.core.database.di

import android.content.Context
import androidx.room.Room
import com.jycra.filmaico.core.database.FilmaicoDatabase
import com.jycra.filmaico.data.history.data.dao.MediaProgressDao
import com.jycra.filmaico.data.media.data.dao.MediaDao
import com.jycra.filmaico.data.stream.data.dao.StreamCacheDao
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
    fun provideMediaDao(database: FilmaicoDatabase): MediaDao =
        database.mediaDao()

    @Provides
    @Singleton
    fun provideMediaProgressDao(database: FilmaicoDatabase): MediaProgressDao =
        database.mediaProgressDao()

    @Provides
    @Singleton
    fun provideStreamCacheDao(database: FilmaicoDatabase): StreamCacheDao =
        database.streamCacheDao()

}