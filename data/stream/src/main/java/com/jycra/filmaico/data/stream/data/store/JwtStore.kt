package com.jycra.filmaico.data.stream.data.store

interface JwtStore {

    suspend fun getJwt(): String?
    suspend fun getJwtExp(): Long

    suspend fun saveJwt(jwt: String, exp: Long)

    suspend fun clearJwt()

}