package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.ResolutionResult
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.repository.StreamRepository
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ResolveStreamUseCase @Inject constructor(
    private val repository: StreamRepository
) {

    suspend operator fun invoke(
        resolutionParams: ResolutionParams,
        lastSession: ResolutionSession? = null,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): Result<ResolutionResult> {

        return try {

            Result.success(repository.resolveStream(
                resolutionParams = resolutionParams,
                lastSession = lastSession,
                onStateChange = onStateChange
            ).first())

        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            onStateChange(StreamExtractionState.Error(e.message ?: "Error desconocido", e))
            Result.failure(e)
        } catch (e: Exception) {
            onStateChange(StreamExtractionState.Error(e.message ?: "Error desconocido", e))
            Result.failure(e)
        }

    }

}