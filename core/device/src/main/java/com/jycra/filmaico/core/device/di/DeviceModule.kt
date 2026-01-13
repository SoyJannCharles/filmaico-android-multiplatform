package com.jycra.filmaico.core.device.di

import com.jycra.filmaico.core.device.DeviceIdProviderImpl
import com.jycra.filmaico.data.user.data.DeviceIdProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Binds
    @Singleton
    abstract fun bindDevideIdProvide(
        deviceIdProviderImpl: DeviceIdProviderImpl
    ): DeviceIdProvider

}