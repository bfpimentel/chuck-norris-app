package dev.pimentel.domain.repositories

interface SearchTermsRepository {
    suspend fun getSearchTerm(): String
    suspend fun getSearchTermByTerm(term: String): List<String>
    suspend fun saveSearchTerm(term: String)
    suspend fun deleteSearchTermByTerm(term: String)
    suspend fun getLastSearchTerms(): List<String>
    suspend fun getNumberOfSearchTerms(): Int
    suspend fun deleteLastSearchTerm()
}
