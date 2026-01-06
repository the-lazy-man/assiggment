package com.example.assiggment.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PeanutApi {
    @POST("api/ClientCabinetBasic/IsAccountCredentialsCorrect")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponseDto>
}
