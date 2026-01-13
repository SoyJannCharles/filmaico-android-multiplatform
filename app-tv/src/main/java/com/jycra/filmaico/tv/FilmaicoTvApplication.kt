package com.jycra.filmaico.tv

import android.app.Application
import com.jycra.filmaico.core.config.ConfigInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FilmaicoTvApplication : Application() {

    @Inject
    lateinit var configInitializer: ConfigInitializer

    override fun onCreate() {
        super.onCreate()
        configInitializer.initialize()
    }

}