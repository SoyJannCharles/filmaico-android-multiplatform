package com.jycra.filmaico.data.stream.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.stream.DrmKeysDto
import com.jycra.filmaico.data.stream.mapper.toDomain
import com.jycra.filmaico.data.stream.mapper.toDto
import com.jycra.filmaico.domain.stream.model.DrmKeys
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeysService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getDrmKey(channelId: String): DrmKeys? {

        return try {
            val document = firestore.collection("channel_drm_keys")
                .document(channelId).get().await()
            if (document.exists()) {
                val dto = document.toObject(DrmKeysDto::class.java)
                dto?.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

    }

    suspend fun saveDrmKey(
        channelId: String,
        keys: DrmKeys
    ) {
        try {
            val dto = keys.toDto()
            firestore.collection("channel_drm_keys").document(channelId).set(dto).await()
        } catch (e: Exception) {
        }
    }

}