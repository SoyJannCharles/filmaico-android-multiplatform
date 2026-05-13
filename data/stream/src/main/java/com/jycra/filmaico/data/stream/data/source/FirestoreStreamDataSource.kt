package com.jycra.filmaico.data.stream.data.source

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.firebase.model.stream.SeedDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreStreamDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : StreamDataSource {

    private val seedCollection = firestore.collection("discovery_pool")

    override suspend fun getSeedsByProvider(provider: String): List<SeedDto> = try {

        val snapshot = seedCollection
            .whereEqualTo("provider", provider)
            .get()
            .await()

        snapshot.toObjects(SeedDto::class.java).also {
            FLog.d(LogCategory.DATABASE, "Retrieved ${it.size} discovery seeds for provider: $provider")
        }

    } catch (e: Exception) {
        FLog.e(LogCategory.DATABASE, "Seed retrieval failed for provider: $provider", e)
        emptyList()
    }

    override suspend fun saveSeed(domain: String, provider: String) {
        try {

            val safeId = domain.replace(".", "_")
            seedCollection.document(safeId).set(
                SeedDto(
                    domain = domain,
                    provider = provider
                )
            ).await()

            FLog.d(LogCategory.DATABASE, "New discovery seed registered: domain=${domain}, provider=${provider}")

        } catch (e: Exception) {
            FLog.e(LogCategory.DATABASE, "Failed to register new seed: ${domain}", e)
        }
    }

    override suspend fun deleteSeed(domain: String, provider: String) {
        try {

            val safeId = domain.replace(".", "_")
            seedCollection.document(safeId).delete().await()

            FLog.d(LogCategory.DATABASE, "Seed successfully removed: domain=$domain, provider=$provider")

        } catch (e: Exception) {
            FLog.e(LogCategory.DATABASE, "Failed to delete seed: $domain", e)
        }
    }

}