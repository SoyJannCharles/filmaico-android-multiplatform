package com.jycra.filmaico.core.app

sealed class AppHealth {
    object Checking : AppHealth()
    object Ready : AppHealth()
    data class UpdateRequired(val current: String, val server: String) : AppHealth()
    data class Error(val message: String) : AppHealth()
}