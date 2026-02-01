package com.jycra.filmaico.data.history.repository

import com.jycra.filmaico.data.history.data.dao.MediaProgressDao
import com.jycra.filmaico.data.history.mapper.toDomain
import com.jycra.filmaico.data.history.mapper.toEntity
import com.jycra.filmaico.domain.history.model.MediaProgress
import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaProgressRepositoryImpl @Inject constructor(
    private val mediaProgressDao: MediaProgressDao
) : MediaProgressRepository {

    override fun getRecentProgressFlow(): Flow<List<MediaProgress>> {
        return mediaProgressDao.getRecentProgress().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecentProgressByOwnerType(ownerMediaType: String, limit: Int): Flow<List<MediaProgress>> {
        return mediaProgressDao.getRecentProgressByOwnerMediaType(ownerMediaType, limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProgressByOwnerId(ownerId: String): Flow<List<MediaProgress>> {
        return mediaProgressDao.getProgressByOwnerFlow(ownerId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProgressByMediaId(mediaId: String): MediaProgress? {
        return mediaProgressDao.getProgressByMediaId(mediaId)?.toDomain()
    }

    override suspend fun upsertProgress(progress: MediaProgress) {
        mediaProgressDao.upsertProgress(progress.toEntity())
    }

    override suspend fun deleteProgressById(mediaId: String) {
        mediaProgressDao.deleteById(mediaId)
    }

    override suspend fun clearAllProgress() {
        mediaProgressDao.clearAll()
    }

}