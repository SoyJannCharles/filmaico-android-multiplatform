package com.jycra.filmaico.feature.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.usecase.DeleteUserUseCase
import com.jycra.filmaico.domain.user.usecase.ListenToSubscriptionStatusUseCase
import com.jycra.filmaico.domain.user.usecase.ReauthenticateAndDeleteUserUseCase
import com.jycra.filmaico.domain.user.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val listenToSubscriptionStatusUseCase: ListenToSubscriptionStatusUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val reauthenticateAndDeleteUserUseCase: ReauthenticateAndDeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PayUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<PayUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        listenForSubscriptionActivation()
    }

    private fun listenForSubscriptionActivation() {
        viewModelScope.launch {

            listenToSubscriptionStatusUseCase()
                .filter { it }
                .collectLatest {
                    _effect.send(PayUiEffect.NavigateToHome)
                }

        }
    }

    fun onEvent(event: PayUiEvent) {
        when(event) {
            PayUiEvent.OnCancelClick -> _uiState.update { it.copy(showDeleteConfirmation = true) }
            PayUiEvent.OnConfirmDelete -> deleteUser()
            PayUiEvent.OnDismissDelete -> _uiState.update { it.copy(showDeleteConfirmation = false) }
            PayUiEvent.OnErrorDismiss -> _uiState.update { it.copy(error = null) }
            is PayUiEvent.OnReauthConfirm -> reauthenticateAndDelete(event.password)
            PayUiEvent.OnReauthDismiss -> _uiState.update { it.copy(showReauthDialog = false, error = null) }
        }
    }

    private fun deleteUser() {
        viewModelScope.launch {

            _uiState.update { it.copy(isDeleting = true, error = null) }

            val result = deleteUserUseCase()

            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isDeleting = false) }
                    _effect.send(PayUiEffect.NavigateToSignIn)
                }
                is AuthResult.Failure -> {
                    if (result.failure == AuthError.RequiresRecentLogin) {
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                showReauthDialog = true,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                error = result.failure
                            )
                        }
                    }
                }
            }

        }
    }

    private fun reauthenticateAndDelete(password: String) {
        viewModelScope.launch {

            _uiState.update { it.copy(isDeleting = true, error = null) }

            val result = reauthenticateAndDeleteUserUseCase(password)

            _uiState.update { it.copy(isDeleting = false) }

            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(showReauthDialog = false) }
                    _effect.send(PayUiEffect.NavigateToSignIn)
                }
                is AuthResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            showReauthDialog = true,
                            error = result.failure
                        )
                    }
                }
            }

        }
    }

}