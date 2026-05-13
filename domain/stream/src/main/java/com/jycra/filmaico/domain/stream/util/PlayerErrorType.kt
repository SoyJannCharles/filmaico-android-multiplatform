package com.jycra.filmaico.domain.stream.util

sealed class PlayerErrorType {
    data object BehindLiveWindow : PlayerErrorType()
    data object ReadPosition : PlayerErrorType()
    data object NetworkInstability : PlayerErrorType()
    data object Decoding : PlayerErrorType()
    data object EdgeDisconnection : PlayerErrorType()
    data object SourceFailed : PlayerErrorType()
    data class Unknown(val code: Int) : PlayerErrorType()
}