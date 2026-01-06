package com.example.assiggment.domain.model

data class AuthResult(
    val success: Boolean,
    val token: String? = null,
    val errorMessage: String? = null
)
