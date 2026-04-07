package com.jycra.filmaico.shared.managers

import android.util.Log
import com.jycra.filmaico.data.stream.data.cache.StreamManifestCache
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlaybackData
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.stream.usecase.PrepareStreamUseCase
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class StreamPreloadManager @Inject constructor(
    private val manifestCache: StreamManifestCache,
    private val prepareStreamUseCase: PrepareStreamUseCase
) {

    private val _extractionState = MutableStateFlow<StreamExtractionState>(StreamExtractionState.Idle)
    val extractionState: StateFlow<StreamExtractionState> = _extractionState.asStateFlow()

    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var preloadingJob: Deferred<Result<PlaybackData>>? = null
    private var currentAssetId: String? = null
    private var currentUrl: String? = null

    private fun isCurrentlyExtracting(): Boolean {
        return when (_extractionState.value) {
            is StreamExtractionState.Idle,
            is StreamExtractionState.Success,
            is StreamExtractionState.Error -> false
            else -> true
        }
    }

    fun startPreload(assetId: String, mediaType: MediaType, source: Stream, forceRefresh: Boolean = false) {

        val url = getUrlFromSource(source)

        if (!forceRefresh && currentAssetId == assetId && currentUrl == url && isCurrentlyExtracting()) {
            return
        }

        preloadingJob?.cancel()

        currentAssetId = assetId
        currentUrl = url
        _extractionState.value = StreamExtractionState.Idle

        preloadingJob = managerScope.async {

            try {

                val result = runNewExtraction(assetId, mediaType, source, forceRefresh)

                result

            } catch (e: CancellationException) {
                throw e
            }

        }

    }

    suspend fun getStream(assetId: String, mediaType: MediaType, source: Stream, forceRefresh: Boolean = false): Result<PlaybackData> {

        val url = getUrlFromSource(source)

        if (!forceRefresh && currentAssetId == assetId && currentUrl == url && preloadingJob != null) {
            try {
                preloadingJob?.await().let { result ->
                    return result!!
                }
            } catch (e: CancellationException) {
                Log.w("StreamPreloadManager", "La precarga fue cancelada en el aire. Reiniciando de forma segura...")
            }
        }

        preloadingJob?.cancel()
        currentAssetId = assetId
        currentUrl = url
        _extractionState.value = StreamExtractionState.Idle

        val deferred = managerScope.async {
            runNewExtraction(assetId, mediaType, source, forceRefresh)
        }

        preloadingJob = deferred

        return deferred.await()

    }

    private suspend fun runNewExtraction(assetId: String, mediaType: MediaType, source: Stream, forceRefresh: Boolean): Result<PlaybackData> {
        return prepareStreamUseCase(assetId, mediaType, source, forceRefresh) { newState ->
            Log.d("StreamPreload", "[${assetId}] -> ${newState.message}")
            _extractionState.value = newState
        }
    }

    private fun getUrlFromSource(source: Stream): String {
        return when (source) {
            is Stream.Direct -> source.uri
            is Stream.WebViewScrap -> source.iframeUrl
        }
    }

    fun prefetch(assetId: String, mediaType: MediaType, source: Stream) {

        if (manifestCache.get(assetId) != null) return

        managerScope.launch(Dispatchers.IO) {
            try {

                val result = prepareStreamUseCase(assetId, mediaType, source, false) {}

                result

            } catch (e: Exception) {

            }
        }

    }

    fun clear() {

        preloadingJob?.cancel()

        preloadingJob = null
        currentAssetId = null
        currentUrl = null

        _extractionState.value = StreamExtractionState.Idle

    }

}