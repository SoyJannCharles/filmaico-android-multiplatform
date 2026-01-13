package com.jycra.filmaico.core.model.user

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import java.util.Date

@Keep
data class SubscriptionDto(
    val expirationDate: Timestamp = Timestamp.now(),
    val maxDevices: Int = 0
) {

    fun isActive(): Boolean =
        expirationDate.toDate().after(Date())

}