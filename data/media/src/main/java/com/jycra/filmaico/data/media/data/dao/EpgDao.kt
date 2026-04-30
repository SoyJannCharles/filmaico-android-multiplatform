package com.jycra.filmaico.data.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.media.entity.EpgEntity

@Dao
interface EpgDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(epgList: List<EpgEntity>)

    @Query("DELETE FROM epg")
    suspend fun clearAll()

    @Transaction
    suspend fun refreshEpg(epgList: List<EpgEntity>) {
        clearAll()
        insertAll(epgList)
    }

    @Query("""
        SELECT * FROM epg 
        WHERE :currentTime >= startTime AND :currentTime < endTime
    """)
    suspend fun getAllCurrentPrograms(currentTime: Long): List<EpgEntity>

}