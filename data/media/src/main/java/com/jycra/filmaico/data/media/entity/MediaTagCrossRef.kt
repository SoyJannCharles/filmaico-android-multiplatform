package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "media_tag_cross_ref",
    primaryKeys = ["mediaId", "tag"],
    indices = [Index("tag")]
)
data class MediaTagCrossRef(
    val mediaId: String,
    val tag: String
)