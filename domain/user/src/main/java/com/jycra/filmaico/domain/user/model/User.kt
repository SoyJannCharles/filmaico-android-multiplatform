package com.jycra.filmaico.domain.user.model

data class User(
    val uid: String,
    val email: String?,
    val subscription: Subscription?,
    val activeSessions: List<Session>
)