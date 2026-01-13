package com.jycra.filmaico.data.stream.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jycra.filmaico.data.stream.entity.DrmKeyEntity

@Dao
interface DrmKeyDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveDrmKey(drmKey: DrmKeyEntity)

    @Query("SELECT * FROM drm_keys WHERE contentId = :contentId")
    suspend fun getDrmKey(contentId: String): DrmKeyEntity?

    @Query("DELETE FROM drm_keys WHERE contentId = :contentId")
    suspend fun invalidateDrmKey(contentId: String)

}