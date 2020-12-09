package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.repositories.FactsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetFacts(
    private val factsRepository: FactsRepository
) : UseCase<GetFacts.Params, List<Fact>> {

    override suspend fun invoke(params: Params): List<Fact> =
        factsRepository.getFacts(params.term).let { response ->
            response.result.map { responseItem ->
                Fact(
                    responseItem.id,
                    responseItem.categories.firstOrNull(),
                    responseItem.url,
                    responseItem.value
                )
            }
        }

    data class Params(
        val term: String
    )
}
