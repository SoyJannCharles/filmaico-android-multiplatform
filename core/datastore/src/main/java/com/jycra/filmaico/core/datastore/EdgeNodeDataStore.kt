package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.data.stream.data.store.EdgeNodeStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EdgeNodeDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : EdgeNodeStore {

    companion object {
        private val KEY_PREFERRED_HOST = stringPreferencesKey("preferred_edge_host")
    }

    override suspend fun getPreferredHost(): String? {
        return dataStore.data.map { it[KEY_PREFERRED_HOST] }.first()
    }

    override suspend fun recordSuccess(host: String) {

        val safeHost = host.trim()
        if (safeHost.isBlank()) return

        dataStore.edit { prefs ->
            prefs[KEY_PREFERRED_HOST] = safeHost
        }

        FLog.d(LogCategory.DATASTORE, "New preferred host saved: $safeHost")

    }

    override suspend fun recordFailure(host: String) {

        dataStore.edit { prefs ->
            prefs.remove(KEY_PREFERRED_HOST)
        }

        FLog.w(LogCategory.DATASTORE, "Preferred host cleared due to failure: $host")

    }

}