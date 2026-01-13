package com.jycra.filmaico.core.model.user

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class UserDto(
    @DocumentId
    val uid: String = "",
    val email: String = "",
    val subscription: SubscriptionDto = SubscriptionDto(),
    val activeSessions: List<SessionDto> = emptyList()
)