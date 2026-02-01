package com.jycra.filmaico.domain.history.model

data class MediaProgress(
    val mediaId: String,
    val seasonId: String?,
    val ownerId: String?,
    val mediaType: String,
    val ownerMediaType: String,
    val name: Map<String, String>,
    val imageUrl: String,
    val order: Int,
    val lastPosition: Long,
    val duration: Long,
    val isFinished: Boolean,
    val lastWatchedMillis: Long,
)