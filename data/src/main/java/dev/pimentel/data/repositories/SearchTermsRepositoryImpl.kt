package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.sources.local.SearchTermsLocalDataSource
import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.models.SearchTerm as DomainSearchTerm

internal class SearchTermsRepositoryImpl(
    private val localDataSource: SearchTermsLocalDataSource
) : SearchTermsRepository {

    override suspend fun getSearchTerm(): DomainSearchTerm =
        localDataSource.getSearchTerm()
            .let { searchTerm -> DomainSearchTerm(searchTerm.term) }

    override suspend fun getSearchTermByTerm(term: String): List<DomainSearchTerm> =
        localDataSource.getSearchTermByTerm(term).let { searchTerms ->
            searchTerms.map { searchTerm -> DomainSearchTerm(searchTerm.term) }
        }

    override suspend fun saveSearchTerm(searchTerm: DomainSearchTerm) =
        localDataSource.insertSearchTerm(
            SearchTerm(term = searchTerm.term)
        )

    override suspend fun deleteSearchTermByTerm(term: String) =
        localDataSource.deleteSearchTermByTerm(term)

    override suspend fun getLastSearchTerms(): List<DomainSearchTerm> =
        localDataSource.getLastSearchTerms().let { searchTerms ->
            searchTerms.map { searchTerm -> DomainSearchTerm(searchTerm.term) }
        }

    override suspend fun getNumberOfSearchTerms(): Int =
        localDataSource.getNumberOfSearchTerms()

    override suspend fun deleteLastSearchTerm() =
        localDataSource.deleteLastSearchTerm()
}
