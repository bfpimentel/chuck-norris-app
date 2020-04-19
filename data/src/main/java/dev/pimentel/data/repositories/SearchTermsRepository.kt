package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.sources.SearchTermsLocalDataSource
import io.reactivex.Single

interface SearchTermsRepository {
    fun getSearchTermByTerm(term: String): Single<List<SearchTerm>>
    fun saveSearchTerm(searchTerm: SearchTerm)
    fun deleteSearchTermByTerm(term: String)
    fun getLastSearchTerms(): Single<List<SearchTerm>>
    fun getNumberOfSearchTerms(): Single<Int>
    fun deleteLastSearchTerm()
}

class SearchTermsRepositoryImpl(
    private val localDataSource: SearchTermsLocalDataSource
) : SearchTermsRepository {

    override fun getSearchTermByTerm(term: String): Single<List<SearchTerm>> =
        localDataSource.getSearchTermsByTerm(term)

    override fun saveSearchTerm(searchTerm: SearchTerm) =
        localDataSource.insertSearchTerm(searchTerm)

    override fun deleteSearchTermByTerm(term: String) =
        localDataSource.deleteSearchTermByTerm(term)

    override fun getLastSearchTerms(): Single<List<SearchTerm>> =
        localDataSource.getLastSearchTerms()

    override fun getNumberOfSearchTerms(): Single<Int> =
        localDataSource.getNumberOfSearchTerms()

    override fun deleteLastSearchTerm() =
        localDataSource.deleteLastSearchTerm()
}
