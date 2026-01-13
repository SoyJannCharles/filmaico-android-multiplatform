package com.jycra.filmaico.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jycra.filmaico.data.anime.data.dao.AnimeCarouselDao
import com.jycra.filmaico.data.anime.data.dao.AnimeDao
import com.jycra.filmaico.data.anime.entity.AnimeCarouselEntity
import com.jycra.filmaico.data.anime.entity.AnimeContentEntity
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity
import com.jycra.filmaico.data.anime.entity.AnimeTagCrossRef
import com.jycra.filmaico.data.anime.entity.AnimeTagEntity
import com.jycra.filmaico.data.channel.data.dao.ChannelCarouselDao
import com.jycra.filmaico.data.channel.data.dao.ChannelDao
import com.jycra.filmaico.data.channel.data.dao.WatchHistoryDao
import com.jycra.filmaico.data.channel.entity.ChannelEntity
import com.jycra.filmaico.data.channel.entity.ChannelCarouselEntity
import com.jycra.filmaico.data.channel.entity.WatchHistoryEntity
import com.jycra.filmaico.data.movie.data.dao.MovieCarouselDao
import com.jycra.filmaico.data.movie.data.dao.MovieDao
import com.jycra.filmaico.data.movie.entity.MovieCarouselEntity
import com.jycra.filmaico.data.movie.entity.MovieEntity
import com.jycra.filmaico.data.serie.data.dao.SerieCarouselDao
import com.jycra.filmaico.data.serie.data.dao.SerieDao
import com.jycra.filmaico.data.serie.entity.SerieCarouselEntity
import com.jycra.filmaico.data.serie.entity.SerieContentEntity
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.data.serie.entity.SerieSeasonEntity
import com.jycra.filmaico.data.serie.entity.SerieTagCrossRef
import com.jycra.filmaico.data.serie.entity.SerieTagEntity
import com.jycra.filmaico.data.stream.data.dao.DrmKeyDao
import com.jycra.filmaico.data.stream.entity.DrmKeyEntity

@Database(
    entities = [

        MovieCarouselEntity::class,
        MovieEntity::class,

        SerieCarouselEntity::class,
        SerieEntity::class,
        SerieSeasonEntity::class,
        SerieContentEntity::class,
        SerieTagEntity::class,
        SerieTagCrossRef::class,

        ChannelCarouselEntity::class,
        ChannelEntity::class,
        WatchHistoryEntity::class,

        AnimeCarouselEntity::class,
        AnimeEntity::class,
        AnimeSeasonEntity::class,
        AnimeContentEntity::class,
        AnimeTagEntity::class,
        AnimeTagCrossRef::class,

        DrmKeyEntity::class

    ],
    version = 1,
    exportSchema = false
)
abstract class FilmaicoDatabase : RoomDatabase() {

    abstract fun movieCarouselDao(): MovieCarouselDao
    abstract fun movieDao(): MovieDao

    abstract fun serieCarouselDao(): SerieCarouselDao
    abstract fun serieDao(): SerieDao

    abstract fun channelCarouselDao(): ChannelCarouselDao
    abstract fun channelDao(): ChannelDao

    abstract fun watchHistoryDao(): WatchHistoryDao

    abstract fun animeCarouselDao(): AnimeCarouselDao
    abstract fun animeDao(): AnimeDao

    abstract fun drmKeyDao(): DrmKeyDao

}