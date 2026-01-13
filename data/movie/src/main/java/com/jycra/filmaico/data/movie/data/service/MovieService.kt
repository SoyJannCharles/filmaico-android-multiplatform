package com.jycra.filmaico.data.movie.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.movie.MovieCarouselDto
import com.jycra.filmaico.core.model.movie.MovieDto
import javax.inject.Inject

class MovieService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getMovieCarouselsRealtime(onUpdate: (List<MovieCarouselDto>) -> Unit) {
        firestore.collection("movie_carousels")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val carousels = snapshot.documents.mapNotNull { document ->
                        document.toObject(MovieCarouselDto::class.java)
                    }
                    onUpdate(carousels)
                }
            }
    }

    fun getMoviesRealtime(onUpdate: (List<MovieDto>) -> Unit) {
        firestore.collection("movies")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null)  {
                    val movies = snapshot.documents.mapNotNull { document ->
                        document.toObject(MovieDto::class.java)
                    }
                    onUpdate(movies)
                }
            }
    }

}