package com.jycra.filmaico.core.navigation.util

import com.google.gson.Gson
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GsonEntryPoint {

    fun getGson(): Gson

}