package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "serie_seasons",
    foreignKeys = [
        ForeignKey(
            entity = SerieEntity::class,
            parentColumns = ["id"],
            childColumns = ["serieOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SerieSeasonEntity(
    @PrimaryKey
    val id: String,
    val seasonNumber: Int,
    val name: String,
    val serieOwnerId: String
)