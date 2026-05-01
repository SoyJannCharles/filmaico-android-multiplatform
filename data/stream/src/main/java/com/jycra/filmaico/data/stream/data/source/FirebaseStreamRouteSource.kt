package com.jycra.filmaico.data.stream.data.source

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.firebase.model.stream.StreamRouteDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStreamRouteSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : StreamRouteSource {

    private val collection = firestore.collection("stream_pool")

    override suspend fun getStreamDomains(provider: String): List<StreamRouteDto> {
        return try {
            collection
                .whereEqualTo("provider", provider)
                .whereEqualTo("dead", false)
                .orderBy("avgResponseMs")
                .get()
                .await()
                .toObjects(StreamRouteDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveStreamDomain(provider: String, domain: String) {

        val doc = collection.document("${provider}_${domain}")
        val snapshot = doc.get().await()

        if (snapshot.exists()) {
            doc.update("dead", false, "lastUsed", FieldValue.serverTimestamp()).await()
        } else {
            doc.set(
                StreamRouteDto(
                    domain = domain,
                    provider = provider,
                    avgResponseMs = 0L,
                    dead = false
                )
            ).await()
        }

    }

    override suspend fun markDomainDead(provider: String, domain: String) {
        TODO("Not yet implemented")
    }

}