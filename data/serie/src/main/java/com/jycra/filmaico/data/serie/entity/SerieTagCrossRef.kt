package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity

@Entity(tableName = "serie_tag_cross_ref", primaryKeys = ["serieId", "tagName"])
data class SerieTagCrossRef(
    val serieId: String,
    val tagName: String
)