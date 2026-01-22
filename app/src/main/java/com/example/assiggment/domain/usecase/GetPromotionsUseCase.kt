package com.example.assiggment.domain.usecase

import com.example.assiggment.domain.model.Promotion
import com.example.assiggment.domain.repository.MainRepository
import javax.inject.Inject

class GetPromotionsUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(): Result<List<Promotion>> {
        return repository.getPromotions()
    }
}
