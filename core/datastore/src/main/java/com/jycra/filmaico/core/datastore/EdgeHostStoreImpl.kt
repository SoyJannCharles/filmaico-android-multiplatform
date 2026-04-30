package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.jycra.filmaico.data.stream.data.store.EdgeHostStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.emptySet

class EdgeHostStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : EdgeHostStore {

    companion object {

        private const val MAX_PREFERRED_HOSTS = 16
        private const val MAX_EXCLUDED_HOSTS = 512

        private val PREFERRED_HOSTS_KEY = stringPreferencesKey("edge_preferred_hosts")
        private val EXCLUDED_HOSTS_KEY = stringSetPreferencesKey("edge_excluded_hosts")

    }

    override suspend fun getPreferredHosts(): List<String> {
        val csv = dataStore.data.map { it[PREFERRED_HOSTS_KEY] ?: "" }.first()
        return if (csv.isBlank()) emptyList() else csv.split(",")
    }

    override suspend fun getExcludedHosts(): Set<String> {
        return dataStore.data.map { it[EXCLUDED_HOSTS_KEY] ?: emptySet() }.first()
    }

    override suspend fun recordSuccess(host: String) {

        val safeHost = host.lowercase()

        dataStore.edit { prefs ->

            val currentCsv = prefs[PREFERRED_HOSTS_KEY] ?: ""
            val currentList = if (currentCsv.isBlank()) emptyList() else currentCsv.split(",")

            val updatedList = currentList.toMutableList().apply {
                remove(safeHost)
                add(0, safeHost)
            }.take(MAX_PREFERRED_HOSTS)

            prefs[PREFERRED_HOSTS_KEY] = updatedList.joinToString(",")

            val excluded = prefs[EXCLUDED_HOSTS_KEY]?.toMutableSet() ?: mutableSetOf()
            if (excluded.remove(safeHost)) {
                prefs[EXCLUDED_HOSTS_KEY] = excluded
            }

        }

    }

    override suspend fun recordFailure(host: String) {

        val safeHost = host.lowercase()

        dataStore.edit { prefs ->

            var excluded = prefs[EXCLUDED_HOSTS_KEY]?.toMutableSet() ?: mutableSetOf()
            excluded.add(safeHost)

            if (excluded.size >= MAX_EXCLUDED_HOSTS) {
                excluded = mutableSetOf()
            }

            prefs[EXCLUDED_HOSTS_KEY] = excluded

            val currentCsv = prefs[PREFERRED_HOSTS_KEY] ?: ""
            if (currentCsv.contains(safeHost)) {
                val currentList = currentCsv.split(",")
                val updatedList = currentList.filterNot { it == safeHost }
                prefs[PREFERRED_HOSTS_KEY] = updatedList.joinToString(",")
            }

        }

    }

}