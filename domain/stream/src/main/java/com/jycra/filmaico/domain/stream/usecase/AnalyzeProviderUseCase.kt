package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.stream.model.metadata.ProviderMetadata
import com.jycra.filmaico.domain.stream.util.M3U8Analyzer
import javax.inject.Inject

class AnalyzeProviderUseCase @Inject constructor(
    private val prepareStreamUseCase: PrepareStreamUseCase
) {

    suspend operator fun invoke(assetId: String, mediaType: MediaType, source: Stream): ProviderMetadata? {

        val result = prepareStreamUseCase(
            assetId = assetId,
            mediaType = mediaType,
            source = source,
            isAnalysisOnly = true
        )

        return result.getOrNull()?.manifestContent?.let {
            M3U8Analyzer.analyze(it)
        }

    }

}