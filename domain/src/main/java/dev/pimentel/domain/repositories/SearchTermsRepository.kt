package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.SearchTerm
import io.reactivex.Single

interface SearchTermsRepository {
    fun getSearchTerm(): Single<SearchTerm>
    fun getSearchTermByTerm(term: String): Single<List<SearchTerm>>
    fun saveSearchTerm(searchTerm: SearchTerm)
    fun deleteSearchTermByTerm(term: String)
    fun getLastSearchTerms(): Single<List<SearchTerm>>
    fun getNumberOfSearchTerms(): Single<Int>
    fun deleteLastSearchTerm()
}
