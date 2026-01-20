package com.example.assiggment.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assiggment.data.local.TokenManager
import com.example.assiggment.domain.usecase.GetProfileUseCase
import com.example.assiggment.domain.usecase.GetPromotionsUseCase
import com.example.assiggment.domain.usecase.GetTradesUseCase
import com.example.assiggment.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getTradesUseCase: GetTradesUseCase,
    private val getPromotionsUseCase: GetPromotionsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        loadData()
    }

    fun loadData() {
        val token = tokenManager.getToken()
        val loginId = tokenManager.getLoginId()

        if (token == null || loginId == -1) {
            logout()
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            
            // Parallel execution would be better but keeping it simple for now
            val profileResult = getProfileUseCase(loginId, token)
            val tradesResult = getTradesUseCase(loginId, token)
            val promotionsResult = getPromotionsUseCase()

            if (profileResult.isSuccess && tradesResult.isSuccess && promotionsResult.isSuccess) {
                state = state.copy(
                    isLoading = false,
                    profile = profileResult.getOrNull(),
                    trades = tradesResult.getOrNull() ?: emptyList(),
                    promotions = promotionsResult.getOrNull() ?: emptyList(),
                    totalProfit = tradesResult.getOrNull()?.sumOf { it.profit } ?: 0.0,
                    error = null
                )
            } else {
                state = state.copy(
                    isLoading = false,
                    error = profileResult.exceptionOrNull()?.message ?: tradesResult.exceptionOrNull()?.message // Show one error
                )
            }
        }
    }

    fun logout() {
        logoutUseCase()
        state = state.copy(isLoggedOut = true)
    }
}
