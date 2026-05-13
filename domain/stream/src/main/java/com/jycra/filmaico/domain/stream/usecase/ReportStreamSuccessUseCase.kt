package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.repository.StreamRepository
import javax.inject.Inject

class ReportStreamSuccessUseCase @Inject constructor(
    private val repository: StreamRepository
) {

    suspend operator fun invoke(session: ResolutionSession) {
        repository.reportSuccess(session)
    }

}