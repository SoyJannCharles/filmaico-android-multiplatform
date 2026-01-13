package com.jycra.filmaico.core.reporter

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsErrorReporter @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
) : ErrorReporter {

    override fun recordException(
        throwable: Throwable,
        context: Map<String, Any>
    ) {
        Log.e("CrashlyticsErrorReporter", "Error reportado a Crashlytics", throwable)
        context.forEach { (key, value) ->
            setCustomKey(key, value)
        }
        crashlytics.recordException(throwable)
    }

    private fun setCustomKey(key: String, value: Any) {
        when (value) {
            is String -> crashlytics.setCustomKey(key, value)
            is Boolean -> crashlytics.setCustomKey(key, value)
            is Int -> crashlytics.setCustomKey(key, value)
            is Long -> crashlytics.setCustomKey(key, value)
            is Float -> crashlytics.setCustomKey(key, value)
            is Double -> crashlytics.setCustomKey(key, value)
            else -> crashlytics.setCustomKey(key, value.toString())
        }
    }

}