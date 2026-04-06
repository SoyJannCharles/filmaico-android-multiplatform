package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.stream.DrmInfo
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Key
import com.jycra.filmaico.domain.stream.repository.PlaybackDataRepository
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.ensureActive
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetDrmKeyUseCase @Inject constructor(
    private val repository: PlaybackDataRepository
) {

    suspend operator fun invoke(
        contentId: String,
        drmInfo: DrmInfo,
        forceRefresh: Boolean = false,
        onStateChange: (StreamExtractionState) -> Unit
    ): DrmKeys? {

        coroutineContext.ensureActive()

        if (!forceRefresh && drmInfo.staticKeys.isValid()) {
            onStateChange(StreamExtractionState.EvaluatingStaticDRMKeys)
            return DrmKeys(
                listOf(
                    Key(
                        kty = "oct",
                        k = drmInfo.staticKeys.k,
                        kid = drmInfo.staticKeys.kid,
                    )
                )
            )
        }

        coroutineContext.ensureActive()

        if (drmInfo.licenseUrl.isNotBlank()) {
            onStateChange(StreamExtractionState.RequestingRemoteDRMKeys)
            return repository.fetchDrmKeys(drmInfo.licenseUrl)
        }

        return null

    }

}