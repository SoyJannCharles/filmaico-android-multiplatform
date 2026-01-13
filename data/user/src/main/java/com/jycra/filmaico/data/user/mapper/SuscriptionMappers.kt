package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.model.user.SubscriptionDto
import com.jycra.filmaico.domain.user.model.Subscription

fun SubscriptionDto.toDomain(): Subscription {
    return Subscription(
        expirationDate = this.expirationDate.toDate(),
        maxDevices = this.maxDevices
    )
}