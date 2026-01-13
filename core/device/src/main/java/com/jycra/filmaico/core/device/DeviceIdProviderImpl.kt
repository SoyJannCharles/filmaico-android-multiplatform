package com.jycra.filmaico.core.device

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.jycra.filmaico.data.user.data.DeviceIdProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceIdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceIdProvider {

    @SuppressLint("HardwareIds")
    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}