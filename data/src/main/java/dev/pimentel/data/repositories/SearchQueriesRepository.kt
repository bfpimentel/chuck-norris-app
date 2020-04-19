package dev.pimentel.data.repositories

import dev.pimentel.data.models.SearchQuery
import dev.pimentel.data.sources.SearchQueriesLocalDataSource
import io.reactivex.Single

interface SearchQueriesRepository {
    fun insertSearchQuery(searchQuery: SearchQuery)
    fun deleteSearchQuery(searchQuery: SearchQuery)
    fun fetchLastSearchQueries(): Single<List<SearchQuery>>
}

class SearchQueriesRepositoryImpl(
    private val remoteDataSource: SearchQueriesLocalDataSource
) : SearchQueriesRepository {

    override fun insertSearchQuery(searchQuery: SearchQuery) =
        remoteDataSource.insertSearchQuery(searchQuery)

    override fun deleteSearchQuery(searchQuery: SearchQuery) =
        remoteDataSource.deleteSearchQuery(searchQuery)

    override fun fetchLastSearchQueries(): Single<List<SearchQuery>> =
        remoteDataSource.fetchLastSearchQueries()
}
