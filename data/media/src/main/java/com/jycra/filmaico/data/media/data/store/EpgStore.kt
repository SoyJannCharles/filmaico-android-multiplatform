package com.jycra.filmaico.data.media.data.store

interface EpgStore {

    suspend fun getLastSyncDate(): String?
    suspend fun saveLastSyncDate(date: String)
    suspend fun clearSyncDate()

}