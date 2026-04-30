package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.model.Epg
import com.jycra.filmaico.domain.media.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCurrentEpgUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    operator fun invoke(): Flow<Map<String, Epg>> = flow {
        while (true) {

            val now = System.currentTimeMillis()
            val snapshot = repository.getCurrentEpgSnapshot(now)

            if (snapshot.isEmpty()) {
                delay(2000)
                continue
            }

            emit(snapshot.associateBy { it.epgId })

            delay(60_000)

        }
    }.flowOn(Dispatchers.IO)

}