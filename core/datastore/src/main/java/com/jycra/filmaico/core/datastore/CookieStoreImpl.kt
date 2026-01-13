package com.jycra.filmaico.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jycra.filmaico.data.stream.data.store.CookieStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CookieStore {

    private object Keys {
        val COOKIE_KEY = stringPreferencesKey("cached_vrioott_cookie")
    }

    override suspend fun getCookie(): String? {
        return dataStore.data.first()[Keys.COOKIE_KEY]
    }

    override suspend fun saveCookie(cookie: String) {
        dataStore.edit { preferences ->
            preferences[Keys.COOKIE_KEY] = cookie
        }
    }

    override suspend fun clearCookie() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.COOKIE_KEY)
        }
    }

}