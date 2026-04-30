package com.jycra.filmaico.data.stream.data.service

interface EdgeNodeService {

    suspend fun fetchCandidates(stableKey: String, excludedHosts: Set<String>): List<String>

}