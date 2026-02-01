package com.jycra.filmaico.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.usecase.RegisterDeviceSessionUseCase
import com.jycra.filmaico.domain.user.usecase.CreateUserUseCase
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
class SignUpViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val registerDeviceSessionUseCase: RegisterDeviceSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SignUpUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.EmailChange -> _uiState.update { it.copy(email = event.email) }
            is SignUpUiEvent.PasswordChange -> _uiState.update { it.copy(password = event.password) }
            SignUpUiEvent.SignUpTriggered -> signUp()
            SignUpUiEvent.SignInTriggered -> signIn()
        }
    }

    private fun signUp() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isBlank() || password.isBlank()) return

        viewModelScope.launch {

            _uiState.update { state -> state.copy(isLoading = true, error = null) }

            val result = createUserUseCase(email, password)
            when (result) {
                is AuthResult.Success -> {
                    try {
                        registerDeviceSessionUseCase()
                        _effect.send(SignUpUiEffect.NavigateToSubscription)
                    } catch (e: Exception) {

                    }
                }
                is AuthResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.failure) }
                }
            }

        }

    }

    private fun signIn() {
        viewModelScope.launch {
            _effect.send(SignUpUiEffect.NavigateToAuth)
        }
    }

}