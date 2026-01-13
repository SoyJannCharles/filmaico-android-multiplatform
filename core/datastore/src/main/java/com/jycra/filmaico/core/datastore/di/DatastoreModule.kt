package com.jycra.filmaico.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jycra.filmaico.core.datastore.CookieStoreImpl
import com.jycra.filmaico.core.datastore.JwtStoreImpl
import com.jycra.filmaico.core.datastore.SessionStoreImpl
import com.jycra.filmaico.data.stream.data.store.CookieStore
import com.jycra.filmaico.data.stream.data.store.JwtStore
import com.jycra.filmaico.data.user.data.store.SessionStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreModule {

    @Binds
    @Singleton
    abstract fun bindSessionStore(
        sessionStoreImpl: SessionStoreImpl
    ): SessionStore

    @Binds
    @Singleton
    abstract fun bindJwtStore(
        jwtStoreImpl: JwtStoreImpl
    ): JwtStore

    @Binds
    @Singleton
    abstract fun bindCookieStore(
        cookieStoreImpl: CookieStoreImpl
    ): CookieStore

    companion object {

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }

    }

}