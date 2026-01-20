package com.example.assiggment.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PeanutApi {
    @POST("api/ClientCabinetBasic/IsAccountCredentialsCorrect")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponseDto>

    @POST("api/ClientCabinetBasic/GetAccountInformation")
    suspend fun getAccountInformation(@Body request: AccountInfoRequest): Response<AccountInfoResponse>

    @POST("api/ClientCabinetBasic/GetLastFourNumbersPhone")
    suspend fun getLastFourNumbersPhone(@Body request: PhoneRequest): Response<String>

    @POST("api/ClientCabinetBasic/GetOpenTrades")
    suspend fun getOpenTrades(@Body request: OpenTradesRequest): Response<List<TradeDto>>
}
