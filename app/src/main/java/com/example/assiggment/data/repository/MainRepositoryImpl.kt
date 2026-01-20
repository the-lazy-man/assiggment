package com.example.assiggment.data.repository

import com.example.assiggment.data.remote.AccountInfoRequest
import com.example.assiggment.data.remote.OpenTradesRequest
import com.example.assiggment.data.remote.PeanutApi
import com.example.assiggment.data.remote.PhoneRequest
import com.example.assiggment.data.remote.SoapManager
import com.example.assiggment.domain.model.Profile
import com.example.assiggment.domain.model.Trade
import com.example.assiggment.domain.model.Promotion
import com.example.assiggment.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: PeanutApi,
    private val soapManager: SoapManager
) : MainRepository {

    override suspend fun getProfile(login: Int, token: String): Result<Profile> {
        return try {
            val accountResponse = api.getAccountInformation(AccountInfoRequest(login, token))
            if (accountResponse.isSuccessful) {
                val account = accountResponse.body()
                if (account != null) {
                    val phoneResponse = api.getLastFourNumbersPhone(PhoneRequest(login, token))
                    val phone = if (phoneResponse.isSuccessful) phoneResponse.body() else "N/A"
                    
                    Result.success(
                        Profile(
                            name = account.name ?: "Unknown",
                            currency = account.currency ?: "USD",
                            balance = account.balance ?: 0.0,
                            phoneLastFour = phone ?: "N/A"
                        )
                    )
                } else {
                    Result.failure(Exception("Account data is null"))
                }
            } else {
                Result.failure(Exception("Failed to fetch account info: ${accountResponse.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOpenTrades(login: Int, token: String): Result<List<Trade>> {
        return try {
            val response = api.getOpenTrades(OpenTradesRequest(login, token))
            if (response.isSuccessful) {
                val tradesDto = response.body()
                val trades = tradesDto?.map { dto ->
                    Trade(
                        ticket = dto.ticket,
                        symbol = dto.symbol ?: "N/A",
                        volume = dto.volume,
                        profit = dto.profit,
                        time = formatTradeDate(dto.time),
                        type = if(dto.type == 0) "Buy" else "Sell"
                    )
                } ?: emptyList()
                Result.success(trades)
            } else {
                Result.failure(Exception("Failed to fetch trades: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun formatTradeDate(rawDate: String?): String {
        if (rawDate.isNullOrEmpty()) return ""
        return try {
            // Input: 2021-11-08T06:35:33 (ISO-like)
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
            val date = inputFormat.parse(rawDate)
            outputFormat.format(date ?: return rawDate)
        } catch (e: Exception) {
            rawDate // Return raw if parsing fails
        }
    }
    override suspend fun getPromotions(): Result<List<Promotion>> {
        return try {
            val promotions = soapManager.getPromotions()
            Result.success(promotions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
