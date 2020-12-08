package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FactsResponse

interface FactsRepository {
    suspend fun getFacts(searchTerm: String): FactsResponse
}
