package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchQuery
import dev.pimentel.data.sources.SearchQueriesLocalDataSource
import io.reactivex.Single

interface SearchQueriesRepository {
    fun insertSearchQuery(searchQuery: SearchQuery)
    fun deleteSearchQuery(searchQuery: SearchQuery)
    fun fetchLastSearchQueries(): Single<List<SearchQuery>>
    fun deleteLastSearchQuery()
}

class SearchQueriesRepositoryImpl(
    private val localDataSource: SearchQueriesLocalDataSource
) : SearchQueriesRepository {

    override fun insertSearchQuery(searchQuery: SearchQuery) =
        localDataSource.insertSearchQuery(searchQuery)

    override fun deleteSearchQuery(searchQuery: SearchQuery) =
        localDataSource.deleteSearchQuery(searchQuery)

    override fun fetchLastSearchQueries(): Single<List<SearchQuery>> =
        localDataSource.fetchLastSearchQueries()

    override fun deleteLastSearchQuery() =
        localDataSource.deleteLastSearchQuery()
}
