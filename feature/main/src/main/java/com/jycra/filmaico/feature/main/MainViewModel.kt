package com.jycra.filmaico.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.app.SessionManager
import com.jycra.filmaico.core.ui.util.formatTimeRemaining
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.usecase.SyncEpgUseCase
import com.jycra.filmaico.domain.media.usecase.SyncMetadataRealtimeUseCase
import com.jycra.filmaico.domain.media.usecase.SyncMetadataSnapshotUseCase
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val syncMetadataSnapshotUseCase: SyncMetadataSnapshotUseCase,
    private val syncMetadataRealtimeUseCase: SyncMetadataRealtimeUseCase,
    private val syncEpgUseCase: SyncEpgUseCase
) : ViewModel() {

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Loading)

    val uiState: StateFlow<MainUiState> = combine(
        sessionManager.sessionStatus,
        _syncStatus
    ) { session, sync ->

        if (sync is SyncStatus.Error) {
            return@combine MainUiState.Error(sync.message)
        }

        if (session is SessionStatus.Checking || sync is SyncStatus.Loading) {
            return@combine MainUiState.Loading
        }

        if (session is SessionStatus.Authenticated) {

            val user = session.user

            val expiry = user.subscription?.expirationDate?.time ?: System.currentTimeMillis()
            val diff = expiry - System.currentTimeMillis()

            MainUiState.Ready(
                expirationTimestamp = expiry,
                expirationText = formatTimeRemaining(diff),
                isSubscriptionUrgent = diff < TimeUnit.DAYS.toMillis(5)
            )

        } else {
            MainUiState.Idle
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState.Loading
    )

    init {
        startGlobalSync()
    }

    private fun startGlobalSync() {

        viewModelScope.launch {

            try {

                syncMetadataSnapshotUseCase(MediaType.CHANNEL)
                _syncStatus.value = SyncStatus.Success

                listOf(MediaType.CHANNEL, MediaType.MOVIE, MediaType.SERIE, MediaType.ANIME).forEach { type ->
                    launch { syncMetadataRealtimeUseCase(type) }
                }

                launch {
                    syncEpgUseCase()
                }

            } catch (e: Exception) {
                _syncStatus.value = SyncStatus.Error(e.message ?: "Error desconocido")
            }

        }

    }

}