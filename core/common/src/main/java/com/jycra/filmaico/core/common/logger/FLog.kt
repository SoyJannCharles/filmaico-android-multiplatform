package com.jycra.filmaico.core.common.logger

import android.util.Log

object FLog {

    private const val GLOBAL_TAG = "FILMAICO_APP"

    fun i(category: LogCategory, message: String) {
        Log.i(GLOBAL_TAG, "[${category.tag}] -> $message")
    }

    fun d(category: LogCategory, message: String) {
        Log.d(GLOBAL_TAG, "[${category.tag}] -> $message")
    }

    fun w(category: LogCategory, message: String) {
        Log.w(GLOBAL_TAG, "[${category.tag}] -> WARNING: $message")
    }

    fun e(category: LogCategory, message: String, throwable: Throwable? = null) {
        Log.e(GLOBAL_TAG, "[${category.tag}] -> ERROR: $message", throwable)
    }

}