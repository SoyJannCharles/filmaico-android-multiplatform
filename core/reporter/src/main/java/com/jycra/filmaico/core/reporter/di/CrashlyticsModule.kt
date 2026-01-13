package com.jycra.filmaico.core.reporter.di

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.jycra.filmaico.core.reporter.CrashlyticsErrorReporter
import com.jycra.filmaico.core.reporter.ErrorReporter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashlyticsModule {

    @Binds
    abstract fun bindErrorReporter(
        crashlyticsErrorReporter: CrashlyticsErrorReporter
    ): ErrorReporter

    companion object {

        @Provides
        @Singleton
        fun provideCrashlytics(): FirebaseCrashlytics {
            return Firebase.crashlytics
        }

    }

}