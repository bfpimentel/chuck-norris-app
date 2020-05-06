package dev.pimentel.data.repositories

import dev.pimentel.data.sources.remote.FactsRemoteDataSource
import dev.pimentel.domain.repositories.FactsRepository
import io.reactivex.Single
import dev.pimentel.domain.models.FactsResponse as DomainFactsResponse

internal class FactsRepositoryImpl(
    private val remoteDataSource: FactsRemoteDataSource
) : FactsRepository {

    override fun getFacts(searchTerm: String): Single<DomainFactsResponse> =
        remoteDataSource.getFacts(searchTerm)
            .map { response ->
                DomainFactsResponse(
                    response.result.map { fact ->
                        DomainFactsResponse.Fact(
                            fact.id,
                            fact.categories,
                            fact.url,
                            fact.value
                        )
                    }
                )
            }
}
