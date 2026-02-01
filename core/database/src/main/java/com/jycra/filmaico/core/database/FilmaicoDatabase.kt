package com.jycra.filmaico.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jycra.filmaico.data.history.data.dao.MediaProgressDao
import com.jycra.filmaico.data.history.entity.MediaProgressEntity
import com.jycra.filmaico.data.media.data.dao.MediaDao
import com.jycra.filmaico.data.media.entity.MediaCarouselEntity
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaSeasonEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import com.jycra.filmaico.data.media.util.converter.MediaConverters
import com.jycra.filmaico.data.stream.data.dao.StreamCacheDao
import com.jycra.filmaico.data.stream.entity.StreamCacheEntity
import com.jycra.filmaico.data.stream.util.DrmKeysConverters

@Database(
    entities = [

        MediaEntity::class,
        MediaSeasonEntity::class,
        MediaCarouselEntity::class,
        MediaTagCrossRef::class,

        MediaProgressEntity::class,

        StreamCacheEntity::class

    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MediaConverters::class, DrmKeysConverters::class)
abstract class FilmaicoDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao

    abstract fun mediaProgressDao(): MediaProgressDao

    abstract fun streamCacheDao(): StreamCacheDao

}