package com.jycra.filmaico.data.anime.data.dao.payload

import com.jycra.filmaico.data.anime.entity.AnimeContentEntity
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity
import com.jycra.filmaico.data.anime.entity.AnimeTagCrossRef
import com.jycra.filmaico.data.anime.entity.AnimeTagEntity

data class AnimeDatabasePayload(
    val anime: AnimeEntity,
    val tags: List<AnimeTagEntity>,
    val crossRefs: List<AnimeTagCrossRef>,
    val seasons: List<AnimeSeasonEntity>,
    val content: List<AnimeContentEntity>
)