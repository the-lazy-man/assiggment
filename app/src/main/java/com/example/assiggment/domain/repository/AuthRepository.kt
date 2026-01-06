package com.example.assiggment.domain.repository

import com.example.assiggment.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(login: Int, password: String): AuthResult
}
