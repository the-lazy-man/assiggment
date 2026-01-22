package com.example.assiggment.data.remote

data class AccountInfoRequest(
    val login: Int,
    val token: String
)

data class AccountInfoResponse(
    val name: String?,
    val currency: String?,
    val balance: Double?,
    val equity: Double?,
    val freeMargin: Double?,
    val leverage: Int?,
    val type: String?,
    val address: String?,
    val city: String?,
    val country: String?,
    val zipCode: String?,
    val phone: String?,
    val email: String?
)

data class PhoneRequest(
    val login: Int,
    val token: String
)

data class PhoneResponse(
    val lastFourNumbersPhone: String?
)

data class OpenTradesRequest(
    val login: Int,
    val token: String
)

data class TradeDto(
    val ticket: Int,
    val login: Int,
    @com.google.gson.annotations.SerializedName("openTime")
    val time: String?,
    val type: Int, // 0 for Buy, 1 for Sell usually, checking swagger would be ideal but assumed from common practice
    val symbol: String?,
    val volume: Double,
    val openPrice: Double,
    val closePrice: Double,
    val sl: Double,
    val tp: Double,
    val profit: Double,
    val comment: String?
)

data class OpenTradesResponse(
    val openTrades: List<TradeDto>?
)
