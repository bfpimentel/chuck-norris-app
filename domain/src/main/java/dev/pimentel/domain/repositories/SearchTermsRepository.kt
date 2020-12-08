package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.SearchTerm

interface SearchTermsRepository {
    suspend fun getSearchTerm(): SearchTerm
    suspend fun getSearchTermByTerm(term: String): List<SearchTerm>
    suspend fun saveSearchTerm(searchTerm: SearchTerm)
    suspend fun deleteSearchTermByTerm(term: String)
    suspend fun getLastSearchTerms(): List<SearchTerm>
    suspend fun getNumberOfSearchTerms(): Int
    suspend fun deleteLastSearchTerm()
}
