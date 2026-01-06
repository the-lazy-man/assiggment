package com.example.assiggment.data.remote

data class LoginResponseDto(
    val result: Boolean,
    val token: String?,
    val secondsLeft: Int?
)
