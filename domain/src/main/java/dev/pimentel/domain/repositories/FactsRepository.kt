package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FactsResponse
import io.reactivex.Single

interface FactsRepository {
    fun getFacts(searchTerm: String): Single<FactsResponse>
}
