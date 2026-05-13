package com.jycra.filmaico.data.stream.data.store

interface EdgeNodeStore {

    suspend fun getPreferredHost(): String?

    suspend fun recordSuccess(host: String)
    suspend fun recordFailure(host: String)

}