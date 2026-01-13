package com.jycra.filmaico.domain.user.model

import java.util.Date

data class Subscription(
    val expirationDate: Date,
    val maxDevices: Int
) {

    fun isActive(): Boolean {
        return expirationDate.after(Date())
    }

}