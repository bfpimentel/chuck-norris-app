package dev.pimentel.data.repositories

import dev.pimentel.data.models.FactsResponse
import dev.pimentel.data.sources.FactsRemoteDataSource
import io.reactivex.Single

interface FactsRepository {
    fun getFacts(searchTerm: String): Single<FactsResponse>
}

internal class FactsRepositoryImpl(
    private val remoteDataSource: FactsRemoteDataSource
) : FactsRepository {

    override fun getFacts(searchTerm: String): Single<FactsResponse> =
        remoteDataSource.getFacts(searchTerm)
}
