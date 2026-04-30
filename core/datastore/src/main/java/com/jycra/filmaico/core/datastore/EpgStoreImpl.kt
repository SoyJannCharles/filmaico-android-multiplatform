package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jycra.filmaico.data.media.data.store.EpgStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpgStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : EpgStore {

    private object Keys {
        val LAST_SYNC_DATE = stringPreferencesKey("last_epg_sync_date")
    }

    override suspend fun getLastSyncDate(): String? {
        return dataStore.data.map { preferences ->
            preferences[Keys.LAST_SYNC_DATE]
        }.firstOrNull()
    }

    override suspend fun saveLastSyncDate(date: String) {
        dataStore.edit { preferences ->
            preferences[Keys.LAST_SYNC_DATE] = date
        }
    }

    override suspend fun clearSyncDate() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.LAST_SYNC_DATE)
        }
    }

}