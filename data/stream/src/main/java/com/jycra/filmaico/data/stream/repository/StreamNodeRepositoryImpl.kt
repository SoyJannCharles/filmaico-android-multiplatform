package com.jycra.filmaico.data.stream.repository

import com.jycra.filmaico.domain.stream.repository.StreamNodeRepository
import javax.inject.Inject

class StreamNodeRepositoryImpl @Inject constructor(

) : StreamNodeRepository {

    override suspend fun getOptimalStreamDomain(
        videoId: String,
        provider: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun reportStreamSuccess(videoId: String, domain: String) {
        TODO("Not yet implemented")
    }

    override suspend fun reportStreamFailure(
        videoId: String,
        domain: String,
        reason: String
    ) {
        TODO("Not yet implemented")
    }

}