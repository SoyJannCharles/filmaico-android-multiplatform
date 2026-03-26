package com.jycra.filmaico.core.model.user

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class UserDto(
    @DocumentId
    val uid: String = "",
    val email: String? = null,
    val phone: String? = null,
    val subscription: SubscriptionDto? = null,
    val activeSessions: List<SessionDto> = emptyList()
)