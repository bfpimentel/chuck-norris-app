package dev.pimentel.data.repositories

import dev.pimentel.data.models.FactModelImpl
import dev.pimentel.data.sources.remote.FactsRemoteDataSource
import dev.pimentel.domain.models.FactModel
import dev.pimentel.domain.repositories.FactsRepository

internal class FactsRepositoryImpl(
    private val remoteDataSource: FactsRemoteDataSource
) : FactsRepository {

    override suspend fun getFacts(searchTerm: String): List<FactModel> =
        remoteDataSource.getFacts(searchTerm).result.map { fact ->
            FactModelImpl(
                id = fact.id,
                categories = fact.categories,
                url = fact.url,
                value = fact.value
            )
        }
}
