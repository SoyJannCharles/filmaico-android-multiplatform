package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.repository.EdgeNodeRepository
import javax.inject.Inject

class ReportSuccessfulPlaybackUseCase @Inject constructor(
    private val repository: EdgeNodeRepository
) {

    suspend operator fun invoke(originalUri: String, resolvedUrl: String) {
        if (resolvedUrl.contains("/tok_") && resolvedUrl.contains(".cvattv.com.ar")) {
            repository.reportSuccessAndPublish(originalUri, resolvedUrl)
        }
    }

}