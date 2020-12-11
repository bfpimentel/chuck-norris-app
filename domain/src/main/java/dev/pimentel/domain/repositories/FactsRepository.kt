package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FactModel

interface FactsRepository {
    suspend fun getFacts(searchTerm: String): List<FactModel>
}
