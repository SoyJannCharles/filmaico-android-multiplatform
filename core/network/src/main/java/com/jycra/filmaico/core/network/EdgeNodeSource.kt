package com.jycra.filmaico.core.network

import android.net.Uri
import com.jycra.filmaico.core.network.api.EdgeNodeApi
import com.jycra.filmaico.data.stream.data.service.EdgeNodeService
import com.jycra.filmaico.data.stream.util.EdgeLatencyProber
import javax.inject.Inject

class EdgeNodeSource @Inject constructor(
    private val api: EdgeNodeApi,
    private val prober: EdgeLatencyProber
) : EdgeNodeService {

    override suspend fun fetchCandidates(
        stableKey: String,
        excludedHosts: Set<String>
    ): List<String> =
         try {

             val excludePart = if (excludedHosts.isNotEmpty()) {
                 val joinedExcludes = excludedHosts.filter { it.isNotBlank() }.joinToString("|")
                 "&exclude=${Uri.encode(joinedExcludes)}"
             } else {
                 ""
             }

             val exactUrl = "http://api.argentinatv.live/digitalplay/apis-protect/edge_pool.php" +
                     "?key=${Uri.encode(stableKey)}" +
                     excludePart

             val response = api.getEdgeNodes(url = exactUrl)

             val rawCandidates = if (response.isSuccessful) {
                 response.body()?.toHostList() ?: emptyList()
             } else {
                 emptyList()
             }

             if (rawCandidates.size >= 2) {
                 prober.sortByLatency(rawCandidates)
             } else {
                 rawCandidates
             }

        } catch (e: Exception) {
            emptyList()
        }


}