package com.example.assiggment.presentation.login

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val error: String? = null
)
