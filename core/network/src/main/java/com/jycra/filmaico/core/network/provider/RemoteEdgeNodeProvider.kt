package com.jycra.filmaico.core.network.provider

import android.net.Uri
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.network.api.EdgeNodeApi
import com.jycra.filmaico.data.stream.data.provider.EdgeNodeProvider
import javax.inject.Inject

class RemoteEdgeNodeProvider @Inject constructor(
    private val api: EdgeNodeApi
) : EdgeNodeProvider {

    override suspend fun fetchCandidates(
        stableKey: String,
        preferredHost: String?
    ): List<String> = try {

        FLog.d(LogCategory.API, "Requesting remote candidates. Key: $stableKey | Preferred: $preferredHost")

        val baseUrl = "http://api.argentinatv.live/digitalplay/apis-protect/edge_pool.php"

        val urlBuilder = StringBuilder(baseUrl)
            .append("?key=").append(Uri.encode(stableKey))

        if (!preferredHost.isNullOrBlank()) {
            urlBuilder.append("&host=").append(Uri.encode(preferredHost))
        }

        val exactUrl = urlBuilder.toString()

        val response = api.getEdgeNodes(url = exactUrl)

        if (response.isSuccessful) {

            val candidates = response.body()?.toHostList() ?: emptyList()

            FLog.d(LogCategory.API, "Remote API Success: Received ${candidates.size} candidates")
            candidates

        } else {

            FLog.e(LogCategory.API, "Remote API Error: Code ${response.code()} for stableKey: $stableKey")
            emptyList()

        }

    } catch (e: Exception) {
        FLog.e(LogCategory.API, "Remote API Failure: ${e.message}", e)
        emptyList()
    }

}