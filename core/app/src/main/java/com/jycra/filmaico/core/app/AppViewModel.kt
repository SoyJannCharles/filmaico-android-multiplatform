package com.jycra.filmaico.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.network.ConnectivityObserver
import com.jycra.filmaico.data.user.util.SessionObserver
import com.jycra.filmaico.domain.user.usecase.SignOutLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    sessionObserver: SessionObserver,
    private val signOutLocalUseCase: SignOutLocalUseCase
) : ViewModel() {

    val globalState = combine(
        connectivityObserver.observe(),
        sessionObserver.observe()
    ) { network, session ->
        GlobalAppState(
            networkStatus = network,
            sessionStatus = session
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GlobalAppState()
    )

    fun signOut() {
        viewModelScope.launch {
            signOutLocalUseCase()
        }
    }

}

