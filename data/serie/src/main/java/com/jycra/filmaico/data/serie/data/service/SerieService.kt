package com.jycra.filmaico.data.serie.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.serie.SerieCarouselDto
import com.jycra.filmaico.core.model.serie.SerieContentDto
import com.jycra.filmaico.core.model.serie.SerieDto
import com.jycra.filmaico.core.model.serie.SerieSeasonDto
import javax.inject.Inject

class SerieService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getSerieCarouselsRealtime(onUpdate: (List<SerieCarouselDto>) -> Unit) {
        firestore.collection("serie_carousels")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val carousels = snapshot.documents.mapNotNull { document ->
                        document.toObject(SerieCarouselDto::class.java)
                    }
                    onUpdate(carousels)
                }
            }
    }

    fun getSeriesRealtime(onUpdate: (List<SerieDto>) -> Unit) {
        firestore.collection("series")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val series = snapshot.documents.mapNotNull { document ->
                        document.toObject(SerieDto::class.java)
                    }
                    onUpdate(series)
                }
            }
    }

    fun getSerieContentRealtime(
        serieId: String,
        onSeasonsUpdate: (List<SerieSeasonDto>) -> Unit,
        onContentUpdate: (seasonId: String, content: List<SerieContentDto>) -> Unit
    ) {

        val seasonsRef = firestore.collection("series").document(serieId).collection("seasons")

        seasonsRef.addSnapshotListener { seasonsSnapshot, _ ->

            val seasonDtos = seasonsSnapshot?.toObjects(SerieSeasonDto::class.java) ?: emptyList()
            onSeasonsUpdate(seasonDtos)

            seasonDtos.forEach { season ->
                season.id?.let { id ->
                    seasonsRef.document(id).collection("content")
                        .addSnapshotListener { contentSnapshot, _ ->
                            val contentDtos = contentSnapshot?.toObjects(SerieContentDto::class.java) ?: emptyList()
                            onContentUpdate(id, contentDtos)
                        }
                }
            }

        }

    }

}