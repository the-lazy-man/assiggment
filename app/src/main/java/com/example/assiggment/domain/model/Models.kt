package com.example.assiggment.domain.model

data class Profile(
    val name: String,
    val currency: String,
    val balance: Double,
    val phoneLastFour: String
)

data class Trade(
    val ticket: Int,
    val symbol: String,
    val volume: Double,
    val profit: Double,
    val time: String,
    val type: String 
)
