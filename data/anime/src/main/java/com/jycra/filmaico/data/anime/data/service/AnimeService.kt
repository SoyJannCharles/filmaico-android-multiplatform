package com.jycra.filmaico.data.anime.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.anime.AnimeCarouselDto
import com.jycra.filmaico.core.model.anime.AnimeContentDto
import com.jycra.filmaico.core.model.anime.AnimeDto
import com.jycra.filmaico.core.model.anime.AnimeSeasonDto
import javax.inject.Inject

class AnimeService @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    fun getAnimeCarouselsRealtime(onUpdate: (List<AnimeCarouselDto>) -> Unit) {
        firestore.collection("anime_carousels")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val carousels = snapshot.documents.mapNotNull { document ->
                        document.toObject(AnimeCarouselDto::class.java)
                    }
                    onUpdate(carousels)
                }
            }
    }

    fun getAnimesRealtime(onUpdate: (List<AnimeDto>) -> Unit) {
        firestore.collection("animes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val animes = snapshot.documents.mapNotNull { document ->
                        document.toObject(AnimeDto::class.java)
                    }
                    onUpdate(animes)
                }
            }
    }

    fun getAnimeContentRealtime(
        animeId: String,
        onSeasonsUpdate: (List<AnimeSeasonDto>) -> Unit,
        onContentUpdate: (seasonId: String, content: List<AnimeContentDto>) -> Unit
    ) {

        val seasonsRef = firestore.collection("animes").document(animeId).collection("seasons")

        seasonsRef.addSnapshotListener { seasonsSnapshot, _ ->

            val seasonDtos = seasonsSnapshot?.toObjects(AnimeSeasonDto::class.java) ?: emptyList()
            onSeasonsUpdate(seasonDtos)

            seasonDtos.forEach { season ->
                season.id?.let { id ->
                    seasonsRef.document(id).collection("content")
                        .addSnapshotListener { contentSnapshot, _ ->
                            val contentDtos = contentSnapshot?.toObjects(AnimeContentDto::class.java) ?: emptyList()
                            onContentUpdate(id, contentDtos)
                        }
                }
            }

        }

    }

}