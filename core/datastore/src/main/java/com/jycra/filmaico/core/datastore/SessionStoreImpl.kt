package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jycra.filmaico.data.user.data.store.SessionStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SessionStore {

    private object Keys {
        val SESSION_ID = stringPreferencesKey("session_id")
    }

    override suspend fun getSessionId(): String? {
        return dataStore.data.map { preferences ->
            preferences[Keys.SESSION_ID]
        }.first()
    }

    override suspend fun saveSessionId(sessionId: String) {
        dataStore.edit { preferences ->
            preferences[Keys.SESSION_ID] = sessionId
        }
    }

    override suspend fun clearSessionId() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.SESSION_ID)
        }
    }

}