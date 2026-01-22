package com.example.assiggment.data.repository

import com.example.assiggment.data.local.TokenManager
import com.example.assiggment.data.remote.LoginRequest
import com.example.assiggment.data.remote.PeanutApi
import com.example.assiggment.domain.model.AuthResult
import com.example.assiggment.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PeanutApi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(login: Int, password: String): AuthResult {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.result) {
                    val token = body.token
                    if (token != null) {
                        tokenManager.saveToken(token)
                        tokenManager.saveLoginId(login)
                        AuthResult(success = true, token = token)
                    } else {
                        AuthResult(success = false, errorMessage = "Token is null")
                    }
                } else {
                    AuthResult(success = false, errorMessage = "Login failed: Invalid credentials or server error")
                }
            } else {
                AuthResult(success = false, errorMessage = "Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.message ?: "Unknown error")
        }
    }
}
