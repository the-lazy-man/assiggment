package com.example.assiggment.domain.usecase

import com.example.assiggment.domain.model.AuthResult
import com.example.assiggment.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(login: Int, password: String): AuthResult {
        return repository.login(login, password)
    }
}
