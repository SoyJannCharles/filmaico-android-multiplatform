package com.jycra.filmaico.core.app

import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.util.SessionStatus

data class GlobalAppState(
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Available,
    val sessionStatus: SessionStatus = SessionStatus.Checking
)