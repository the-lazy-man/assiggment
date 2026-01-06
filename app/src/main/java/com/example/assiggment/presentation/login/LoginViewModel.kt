package com.example.assiggment.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assiggment.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onLoginChanged(value: String) {
        state = state.copy(login = value)
    }

    fun onPasswordChanged(value: String) {
        state = state.copy(password = value)
    }

    fun onLoginClick() {
        val loginInt = state.login.toIntOrNull()
        if (loginInt == null) {
            state = state.copy(error = "Login must be a number")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = loginUseCase(loginInt, state.password)
            state = if (result.success) {
                state.copy(isLoading = false, isLoginSuccess = true, error = null)
            } else {
                state.copy(isLoading = false, isLoginSuccess = false, error = result.errorMessage)
            }
        }
    }
}
