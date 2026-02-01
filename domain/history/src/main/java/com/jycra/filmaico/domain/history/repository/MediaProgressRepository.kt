package com.jycra.filmaico.domain.history.repository

import com.jycra.filmaico.domain.history.model.MediaProgress
import kotlinx.coroutines.flow.Flow

interface MediaProgressRepository {

    fun getRecentProgressFlow(): Flow<List<MediaProgress>>

    fun getRecentProgressByOwnerType(ownerMediaType: String, limit: Int = 20): Flow<List<MediaProgress>>
    fun getProgressByOwnerId(ownerId: String): Flow<List<MediaProgress>>

    suspend fun getProgressByMediaId(mediaId: String): MediaProgress?

    suspend fun upsertProgress(progress: MediaProgress)

    suspend fun deleteProgressById(mediaId: String)
    suspend fun clearAllProgress()

}