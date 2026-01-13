package com.jycra.filmaico.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.usecase.SignUpUseCase
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
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SignUpUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.OnEmailChange -> _uiState.update { it.copy(email = event.email) }
            is SignUpUiEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.password) }
            SignUpUiEvent.OnSignUpClick -> signUp()
            SignUpUiEvent.OnSignInClick -> signIn()
        }
    }

    private fun signUp() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isBlank() || password.isBlank()) return

        viewModelScope.launch {

            _uiState.update { state ->
                state.copy(isLoading = true, error = null)
            }

            val result = signUpUseCase(email, password)

            _uiState.update { state ->
                state.copy(isLoading = false)
            }

            when (result) {
                is AuthResult.Success -> _effect.send(SignUpUiEffect.NavigateToPay)
                is AuthResult.Failure -> _uiState.update { it.copy(error = result.failure) }
            }

        }

    }

    private fun signIn() {
        viewModelScope.launch {
            _effect.send(SignUpUiEffect.NavigateToSignIn)
        }
    }

}