package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.sources.local.SearchTermsLocalDataSource
import dev.pimentel.domain.repositories.SearchTermsRepository
import io.reactivex.Single
import dev.pimentel.domain.models.SearchTerm as DomainSearchTerm

internal class SearchTermsRepositoryImpl(
    private val localDataSource: SearchTermsLocalDataSource
) : SearchTermsRepository {

    override fun getSearchTerm(): Single<DomainSearchTerm> =
        localDataSource.getSearchTerm()
            .map { searchTerm -> DomainSearchTerm(searchTerm.term) }

    override fun getSearchTermByTerm(term: String): Single<List<DomainSearchTerm>> =
        localDataSource.getSearchTermByTerm(term).map { searchTerms ->
            searchTerms.map { searchTerm -> DomainSearchTerm(searchTerm.term) }
        }

    override fun saveSearchTerm(searchTerm: DomainSearchTerm) =
        localDataSource.insertSearchTerm(
            SearchTerm(term = searchTerm.term)
        )

    override fun deleteSearchTermByTerm(term: String) =
        localDataSource.deleteSearchTermByTerm(term)

    override fun getLastSearchTerms(): Single<List<DomainSearchTerm>> =
        localDataSource.getLastSearchTerms().map { searchTerms ->
            searchTerms.map { searchTerm -> DomainSearchTerm(searchTerm.term) }
        }

    override fun getNumberOfSearchTerms(): Single<Int> =
        localDataSource.getNumberOfSearchTerms()

    override fun deleteLastSearchTerm() =
        localDataSource.deleteLastSearchTerm()
}
