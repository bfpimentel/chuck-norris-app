package dev.pimentel.data.repositories

import dev.pimentel.data.dto.SearchTermDTO
import dev.pimentel.data.sources.local.SearchTermsLocalDataSource
import dev.pimentel.domain.repositories.SearchTermsRepository

internal class SearchTermsRepositoryImpl(
    private val localDataSource: SearchTermsLocalDataSource
) : SearchTermsRepository {

    override suspend fun getSearchTerm(): String =
        localDataSource.getSearchTerm().term

    override suspend fun getSearchTermByTerm(term: String): List<String> =
        localDataSource.getSearchTermByTerm(term).map(SearchTermDTO::term)

    override suspend fun saveSearchTerm(term: String) =
        localDataSource.insertSearchTerm(searchTerm = SearchTermDTO(term = term))

    override suspend fun deleteSearchTermByTerm(term: String) =
        localDataSource.deleteSearchTermByTerm(term)

    override suspend fun getLastSearchTerms(): List<String> =
        localDataSource.getLastSearchTerms().map(SearchTermDTO::term)

    override suspend fun getNumberOfSearchTerms(): Int =
        localDataSource.getNumberOfSearchTerms()

    override suspend fun deleteLastSearchTerm() =
        localDataSource.deleteLastSearchTerm()
}
