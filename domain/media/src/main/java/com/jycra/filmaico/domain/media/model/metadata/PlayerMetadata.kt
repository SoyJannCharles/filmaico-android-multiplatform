package com.jycra.filmaico.domain.media.model.metadata

import com.jycra.filmaico.domain.history.model.MediaProgress
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.stream.Stream

data class PlayerMetadata(
    val assetId: String,
    val seasonId: String? = null,
    val ownerId: String,
    val mediaType: MediaType,
    val ownerMediaType: MediaType,
    val name: Map<String, String>,
    val imageUrl: String,
    val isSaved: Boolean,
    val order: Int,
    val duration: Long,
    val lastPosition: Long,
    val isFinished: Boolean,
    val isLive: Boolean = false,
    val nextContentId: String? = null,
    val prevContentId: String? = null,
    val sources: List<Stream>
) {

    fun toVideoMetadata() = VideoMetadata(
        name = name,
        order = order,
        isLive = isLive,
        isSaved = isSaved
    )

    fun toMediaProgress(
        thumbnailPath: String?,
        currentPos: Long,
        totalDuration: Long,
        isFinished: Boolean
    ): MediaProgress {
        return MediaProgress(
            mediaId = this.assetId,
            seasonId = this.seasonId,
            ownerId = this.ownerId ?: this.assetId,
            mediaType = this.mediaType.value,
            ownerMediaType = this.ownerMediaType.value,
            name = this.name,
            imageUrl = mapOf("default" to (thumbnailPath ?: this.imageUrl)),
            order = this.order,
            lastPosition = currentPos,
            duration = totalDuration / 1000,
            isFinished = isFinished,
            lastWatchedMillis = System.currentTimeMillis()
        )
    }

}