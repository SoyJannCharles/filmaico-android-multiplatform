package com.jycra.filmaico.core.common

import kotlinx.coroutines.CompletableDeferred

object AppInitializer {
    val remoteConfigReady = CompletableDeferred<Boolean>()
}