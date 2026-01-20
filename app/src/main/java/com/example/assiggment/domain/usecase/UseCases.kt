package com.example.assiggment.domain.usecase

import com.example.assiggment.data.local.TokenManager
import com.example.assiggment.domain.model.Profile
import com.example.assiggment.domain.model.Trade
import com.example.assiggment.domain.repository.MainRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(login: Int, token: String): Result<Profile> {
        return repository.getProfile(login, token)
    }
}

class GetTradesUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(login: Int, token: String): Result<List<Trade>> {
        return repository.getOpenTrades(login, token)
    }
}

class LogoutUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    operator fun invoke() {
        tokenManager.clear()
    }
}
