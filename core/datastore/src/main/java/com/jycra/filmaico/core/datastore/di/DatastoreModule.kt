package com.jycra.filmaico.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jycra.filmaico.core.datastore.CookieStoreImpl
import com.jycra.filmaico.core.datastore.EdgeHostStoreImpl
import com.jycra.filmaico.core.datastore.EpgStoreImpl
import com.jycra.filmaico.core.datastore.JwtStoreImpl
import com.jycra.filmaico.core.datastore.SessionStoreImpl
import com.jycra.filmaico.data.media.data.store.EpgStore
import com.jycra.filmaico.data.stream.data.store.CookieStore
import com.jycra.filmaico.data.stream.data.store.EdgeHostStore
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
        impl: SessionStoreImpl
    ): SessionStore

    @Binds
    @Singleton
    abstract fun bindJwtStore(
        impl: JwtStoreImpl
    ): JwtStore

    @Binds
    @Singleton
    abstract fun bindCookieStore(
        impl: CookieStoreImpl
    ): CookieStore

    @Binds
    @Singleton
    abstract fun bindEdgeHostStore(
        impl: EdgeHostStoreImpl
    ): EdgeHostStore

    @Binds
    @Singleton
    abstract fun bindEpgStore(
        impl: EpgStoreImpl
    ): EpgStore

    companion object {

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }

    }

}