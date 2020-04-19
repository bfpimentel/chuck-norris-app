package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.sources.SearchTermsLocalDataSource
import io.reactivex.Single

interface SearchTermsRepository {
    fun fetchSearchTermByTerm(term: String): Single<List<SearchTerm>>
    fun insertSearchTerm(searchTerm: SearchTerm)
    fun deleteSearchTermByTerm(term: String)
    fun fetchLastSearchTerms(): Single<List<SearchTerm>>
    fun getNumberOfSearchTerms(): Single<Int>
    fun deleteLastSearchTerm()
}

class SearchTermsRepositoryImpl(
    private val localDataSource: SearchTermsLocalDataSource
) : SearchTermsRepository {

    override fun fetchSearchTermByTerm(term: String): Single<List<SearchTerm>> =
        localDataSource.fetchSearchTermsByTerm(term)

    override fun insertSearchTerm(searchTerm: SearchTerm) =
        localDataSource.insertSearchTerm(searchTerm)

    override fun deleteSearchTermByTerm(term: String) =
        localDataSource.deleteSearchTermByTerm(term)

    override fun fetchLastSearchTerms(): Single<List<SearchTerm>> =
        localDataSource.fetchLastSearchTerms()

    override fun getNumberOfSearchTerms(): Single<Int> =
        localDataSource.getNumberOfSearchTerms()

    override fun deleteLastSearchTerm() =
        localDataSource.deleteLastSearchTerm()
}
