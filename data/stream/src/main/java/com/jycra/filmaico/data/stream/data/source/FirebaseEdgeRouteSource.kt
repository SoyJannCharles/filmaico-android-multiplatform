package com.jycra.filmaico.data.stream.data.source

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.firebase.model.stream.EdgeRouteDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseEdgeRouteSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : EdgeRouteSource {

    private val edgeCollection = firestore.collection("edge_pool")

    override suspend fun getEdgeRoute(stableKey: String): EdgeRouteDto? = try {

        val safeId = stableKey
            .replace("/", "_")
            .replace(".", "_")

        val snapshot = edgeCollection.document(safeId).get().await()
        if (snapshot.exists()) {
            snapshot.toObject(EdgeRouteDto::class.java)
        } else {
            null
        }

    } catch (e: Exception) {
        Log.e("FirebaseEdge", "Error descargando ruta: ${e.message}")
        null
    }

    override suspend fun saveEdgeRoute(
        stableKey: String,
        resolvedUrl: String,
        expiration: Long
    ) {

        try {

            val safeId = stableKey
                .replace("/", "_")
                .replace(".", "_")

            edgeCollection.document(safeId).set(
                EdgeRouteDto(
                    url = resolvedUrl,
                    expiresAt = Timestamp(expiration, 0)
                )
            ).await()

            Log.d("FirebaseEdge", "🌱 Ruta sembrada en Firebase para: $safeId")
        } catch (e: Exception) {
            Log.e("FirebaseEdge", "Error subiendo ruta: ${e.message}")
        }

    }

    override suspend fun reportEdgeFailure(
        stableKey: String,
        failedHost: String,
        reason: String
    ) {

        val safeId = stableKey
            .replace("/", "_")
            .replace(".", "_")

        try {
            edgeCollection.document(safeId).delete().await()
            Log.d("FirebaseEdge", "🗑️ Ruta eliminada de Firebase por fallo: $safeId")
        } catch (e: Exception) {
            Log.e("FirebaseEdge", "Error al limpiar ruta fallida en Firebase: ${e.message}")
        }

    }

}