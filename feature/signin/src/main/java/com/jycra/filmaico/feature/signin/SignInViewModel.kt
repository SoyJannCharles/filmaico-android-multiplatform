package com.jycra.filmaico.feature.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.usecase.HasActiveSubscriptionUseCase
import com.jycra.filmaico.domain.user.usecase.RegisterDeviceSessionUseCase
import com.jycra.filmaico.domain.user.usecase.AuthenticateWithEmailUseCase
import com.jycra.filmaico.domain.user.usecase.AuthenticateWithTokenUseCase
import com.jycra.filmaico.domain.user.usecase.CreateAuthSessionUseCase
import com.jycra.filmaico.domain.user.usecase.ObserveAuthSessionStatusUseCase
import com.jycra.filmaico.domain.user.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticateWithEmailUseCase: AuthenticateWithEmailUseCase,
    private val authenticateWithTokenUseCase: AuthenticateWithTokenUseCase,
    private val createAuthSessionUseCase: CreateAuthSessionUseCase,
    private val observeAuthSessionStatusUseCase: ObserveAuthSessionStatusUseCase,
    private val registerDeviceSessionUseCase: RegisterDeviceSessionUseCase,
    private val hasActiveSubscriptionUseCase: HasActiveSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SignInUiEffect>()
    val effect = _effect.receiveAsFlow()

    private var isSessionCreated = false
    private var isAuthenticating = false

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.email, error = null) }
            is SignInUiEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.password, error = null) }
            SignInUiEvent.OnCodeExpired -> {

            }
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

            val result = authenticateWithEmailUseCase(email, password)
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

    fun createAuthSession() {

        if (isSessionCreated) return

        isSessionCreated = true

        viewModelScope.launch {
            when (val result = createAuthSessionUseCase()) {
                is AuthResult.Success -> {
                    val generatedCode = result.data
                    observeAuthSessionStatusUseCase(generatedCode)
                        .onEach { session ->
                            _uiState.update { it.copy(
                                authSessionStatus = session.status,
                                authCode = generatedCode,
                                authCodeExpiresAt = session.expiresAt
                            ) }
                            session.token?.let { token ->
                                signInWithToken(generatedCode, token)
                            }
                        }
                        .catch { e ->
                            _uiState.update { it.copy(error = AuthError.NetworkError) }
                        }
                        .launchIn(viewModelScope)
                }
                is AuthResult.Failure -> {
                    isSessionCreated = false
                    _uiState.update { it.copy(error = result.failure, isLoading = false) }
                }
            }
        }
    }

    private suspend fun signInWithToken(code: String, token: String) {

        if (isAuthenticating) return

        isAuthenticating = true

        when (val result = authenticateWithTokenUseCase(code, token)) {
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
            is AuthResult.Failure -> {
                _uiState.update { it.copy(error = result.failure) }
            }
        }

        isAuthenticating = false

    }

}