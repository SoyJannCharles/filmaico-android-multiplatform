package com.jycra.filmaico.data.media.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.media.MediaCarouselDto
import com.jycra.filmaico.core.model.media.MediaDto
import com.jycra.filmaico.core.model.media.MediaSeasonDto
import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MediaService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getCarousels(type: MediaType): Flow<List<MediaCarouselDto>> = callbackFlow {

        val collectionPath = "${type.value}_carousels"

        val subscription = firestore.collection(collectionPath)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val carousels = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MediaCarouselDto::class.java)
                } ?: emptyList()

                trySend(carousels)

            }

        awaitClose { subscription.remove() }

    }

    fun getMediaContainers(type: MediaType): Flow<List<DocumentSnapshot>> = callbackFlow {

        val collectionPath = "${type.value}s"

        val subscription = firestore.collection(collectionPath)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                trySend(snapshot?.documents ?: emptyList())

            }

        awaitClose { subscription.remove() }

    }

    suspend fun getMediaSeasons(containerId: String, type: MediaType): List<MediaSeasonDto> {

        val collectionPath = "${type.value}s"

        return try {

            val snapshot = firestore.collection(collectionPath)
                .document(containerId)
                .collection("seasons")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(MediaSeasonDto::class.java)?.copy(id = doc.id)
            }

        } catch (e: Exception) {
            emptyList()
        }

    }

    suspend fun getMediaAssets(
        containerId: String,
        seasonId: String,
        type: MediaType
    ): List<MediaDto> {

        return try {

            val collectionPath = "${type.value}s"

            val snapshot = firestore.collection(collectionPath)
                .document(containerId)
                .collection("seasons")
                .document(seasonId)
                .collection("content")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(MediaDto::class.java)?.copy(id = doc.id)
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

}