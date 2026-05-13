package com.jycra.filmaico.data.stream.data.provider

interface EdgeNodeProvider {

    suspend fun fetchCandidates(stableKey: String, preferredHost: String? = null): List<String>

}