package com.jycra.filmaico.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.usecase.HasActiveSubscriptionUseCase
import com.jycra.filmaico.domain.user.usecase.RegisterDeviceSessionUseCase
import com.jycra.filmaico.domain.user.usecase.AuthenticateUserUseCase
import com.jycra.filmaico.domain.user.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val registerDeviceSessionUseCase: RegisterDeviceSessionUseCase,
    private val hasActiveSubscriptionUseCase: HasActiveSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SignInUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.email, error = null) }
            is SignInUiEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.password, error = null) }
            SignInUiEvent.SignInTriggered -> signIn()
            SignInUiEvent.SignUpTriggered -> signUp()
        }
    }

    private fun signIn() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isBlank() || password.isBlank()) return

        viewModelScope.launch {

            _uiState.update { state -> state.copy(isLoading = true, error = null) }

            val result = authenticateUserUseCase(email, password)
            when (result) {
                is AuthResult.Success -> {
                    try {
                        registerDeviceSessionUseCase()
                        if (hasActiveSubscriptionUseCase()) {
                            _effect.send(SignInUiEffect.NavigateToMain)
                        } else {
                            _effect.send(SignInUiEffect.NavigateToSubscription)
                        }
                    } catch (e: Exception) {

                    }
                }
                is AuthResult.Failure ->
                    _uiState.update { it.copy(isLoading = false, error = result.failure) }
            }

        }

    }

    private fun signUp() {
        viewModelScope.launch {
            _effect.send(SignInUiEffect.NavigateToSignUp)
        }
    }

}