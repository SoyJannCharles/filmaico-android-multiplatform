package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jycra.filmaico.data.stream.data.store.JwtStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): JwtStore {

    private object Keys {
        val JWT_KEY = stringPreferencesKey("cached_jwt")
        val JWT_EXP_KEY = longPreferencesKey("cached_jwt_exp")
    }

    override suspend fun getJwt(): String? {
        return dataStore.data.first()[Keys.JWT_KEY]
    }

    override suspend fun getJwtExp(): Long {
        return dataStore.data.first()[Keys.JWT_EXP_KEY] ?: 0L
    }

    override suspend fun saveJwt(jwt: String, exp: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.JWT_KEY] = jwt
            preferences[Keys.JWT_EXP_KEY] = exp
        }
    }

    override suspend fun clearJwt() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.JWT_KEY)
            preferences.remove(Keys.JWT_EXP_KEY)
        }
    }

}