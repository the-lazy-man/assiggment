package com.example.assiggment.presentation.main

import com.example.assiggment.domain.model.Profile
import com.example.assiggment.domain.model.Trade
import com.example.assiggment.domain.model.Promotion

data class MainState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val trades: List<Trade> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val totalProfit: Double = 0.0,
    val error: String? = null,
    val isLoggedOut: Boolean = false
)
