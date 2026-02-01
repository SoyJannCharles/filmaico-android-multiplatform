package com.jycra.filmaico.core.app

import com.jycra.filmaico.core.network.ConnectivityObserver
import com.jycra.filmaico.data.user.util.SessionObserver

data class GlobalAppState(
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Available,
    val sessionStatus: SessionObserver.SessionStatus = SessionObserver.SessionStatus.Loading
)