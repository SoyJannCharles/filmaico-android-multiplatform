package com.jycra.filmaico.core.model.user

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@Keep
@IgnoreExtraProperties
data class SubscriptionDto(
    val maxDevices: Int = 1,
    val expirationDate: Timestamp? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {

    fun isActive(): Boolean {
        return expirationDate?.let {
            it.toDate().after(Date())
        } ?: false
    }

}