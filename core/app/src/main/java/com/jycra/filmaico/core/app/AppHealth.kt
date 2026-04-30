package com.jycra.filmaico.core.app

sealed class AppHealth {
    object Checking : AppHealth()
    data class Ready(val version: String) : AppHealth()
    data class UpdateRequired(val current: String, val server: String) : AppHealth()
    data class Error(val message: String) : AppHealth()
}