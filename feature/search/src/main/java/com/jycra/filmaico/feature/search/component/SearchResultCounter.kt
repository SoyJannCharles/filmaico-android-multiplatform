package com.jycra.filmaico.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.ui.feature.search.UiSearchCarousel

@Composable
fun SearchResultCounter(
    carousels: List<UiSearchCarousel>
) {

    val totalResults = carousels.sumOf { it.items.size }
    val channelCount = carousels.find { it.contentType == ContentType.CHANNEL }?.items?.size ?: 0
    val movieCount = carousels.find { it.contentType == ContentType.MOVIE }?.items?.size ?: 0
    val serieCount = carousels.find { it.contentType == ContentType.SERIE }?.items?.size ?: 0
    val animeCount = carousels.find { it.contentType == ContentType.ANIME }?.items?.size ?: 0

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Resultados: $totalResults" // Usamos el total calculado
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Canales: $channelCount"
            )

            Text(style = MaterialTheme.typography.bodyMedium, text = "|")
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Peliculas: $movieCount"
            )

            Text(style = MaterialTheme.typography.bodyMedium, text = "|")

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Series: $serieCount"
            )

            Text(style = MaterialTheme.typography.bodyMedium, text = "|")

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Animes: $animeCount"
            )

        }

    }

}