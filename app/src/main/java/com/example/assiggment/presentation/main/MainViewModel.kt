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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

        // Set loading and CLEAR data to satisfy "Hide all data" on reload
        state = state.copy(
            isLoading = true, 
            profile = null, 
            trades = emptyList(), 
            promotions = emptyList(), 
            error = null
        )

        val jobProfile = viewModelScope.launch {
            val result = getProfileUseCase(loginId, token)
            if (result.isSuccess) {
                state = state.copy(profile = result.getOrNull())
            } else {
                // Keep track of error but maybe don't overwrite if another succeeded
                 state = state.copy(error = result.exceptionOrNull()?.message)
            }
        }

        val jobTrades = viewModelScope.launch {
            val result = getTradesUseCase(loginId, token)
            if (result.isSuccess) {
                val trades = result.getOrNull() ?: emptyList()
                state = state.copy(
                    trades = trades,
                    totalProfit = trades.sumOf { it.profit }
                )
            } else {
                state = state.copy(error = result.exceptionOrNull()?.message)
            }
        }

        // Promotions - Strictly independent, does not affect isLoading (Spinner)
        viewModelScope.launch {
            val result = getPromotionsUseCase()
            if (result.isSuccess) {
                state = state.copy(promotions = result.getOrNull() ?: emptyList())
            }
        }

        // Monitor Critical Jobs (Profile & Trades) to toggle "isLoading"
        viewModelScope.launch {
            // join() waits for the job to complete
            jobProfile.join()
            jobTrades.join()
            // Once both critical parts are returned (success or fail), stop the spinner
            state = state.copy(isLoading = false)
        }
    }

    fun logout() {
        logoutUseCase()
        state = state.copy(isLoggedOut = true)
    }
}
