package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.firebase.model.user.SubscriptionDto
import com.jycra.filmaico.domain.user.model.Subscription
import java.util.Date

fun SubscriptionDto.toDomain(): Subscription {
    return Subscription(
        expirationDate = this.expirationDate?.toDate() ?: Date(),
        maxDevices = this.maxDevices
    )
}