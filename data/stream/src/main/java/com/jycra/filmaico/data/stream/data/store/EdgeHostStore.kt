package com.jycra.filmaico.data.stream.data.store

interface EdgeHostStore {

    suspend fun getPreferredHosts(): List<String>
    suspend fun getExcludedHosts(): Set<String>
    suspend fun recordSuccess(host: String)
    suspend fun recordFailure(host: String)

}