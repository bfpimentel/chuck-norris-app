package dev.pimentel.data.repositories

import dev.pimentel.data.sources.remote.FactsRemoteDataSource
import dev.pimentel.domain.repositories.FactsRepository
import dev.pimentel.domain.models.FactsResponse as DomainFactsResponse

internal class FactsRepositoryImpl(
    private val remoteDataSource: FactsRemoteDataSource
) : FactsRepository {

    override suspend fun getFacts(searchTerm: String): DomainFactsResponse =
        DomainFactsResponse(
            result = remoteDataSource.getFacts(searchTerm).result
                .map { fact ->
                    DomainFactsResponse.Fact(
                        fact.id,
                        fact.categories,
                        fact.url,
                        fact.value
                    )
                }
        )
}
