package com.example.assiggment.domain.repository

import com.example.assiggment.domain.model.Profile
import com.example.assiggment.domain.model.Trade
import com.example.assiggment.domain.model.Promotion

interface MainRepository {
    suspend fun getProfile(login: Int, token: String): Result<Profile>
    suspend fun getOpenTrades(login: Int, token: String): Result<List<Trade>>
    suspend fun getPromotions(): Result<List<Promotion>>
}
