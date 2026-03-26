package com.jycra.filmaico.domain.user.model

enum class AuthStatus(val value: String) {

    PENDING("pending"),
    COMPLETED("completed"),
    ERROR("error");

    companion object {
        fun fromString(value: String?): AuthStatus {
            return entries.find { it.value == value } ?: PENDING
        }
    }

}