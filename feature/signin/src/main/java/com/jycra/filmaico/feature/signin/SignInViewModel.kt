package com.jycra.filmaico.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.usecase.HasActiveSubscriptionUseCase
import com.jycra.filmaico.domain.user.usecase.SignInUseCase
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
    private val signInUseCase: SignInUseCase,
    private val hasActiveSubscriptionUseCase: HasActiveSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SignInUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.OnEmailChange -> _uiState.update {
                it.copy(email = event.email)
            }
            is SignInUiEvent.OnPasswordChange -> _uiState.update {
                it.copy(password = event.password)
            }
            SignInUiEvent.OnSignInClick -> signIn()
            SignInUiEvent.OnSignUpClick -> signUp()
        }
    }

    private fun signIn() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isBlank() || password.isBlank()) return

        viewModelScope.launch {

            _uiState.update { state ->
                state.copy(isLoading = true, error = null)
            }

            val result = signInUseCase(email, password)

            _uiState.update { state ->
                state.copy(isLoading = false)
            }

            when (result) {
                is AuthResult.Success -> checkSubscriptionAndNavigate()
                is AuthResult.Failure -> _uiState.update { it.copy(error = result.failure) }
            }

        }

    }

    private fun checkSubscriptionAndNavigate() {
        viewModelScope.launch {
            if (hasActiveSubscriptionUseCase()) {
                _effect.send(SignInUiEffect.NavigateToHome)
            } else {
                _effect.send(SignInUiEffect.NavigateToPay)
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _effect.send(SignInUiEffect.NavigateToSignUp)
        }
    }

}