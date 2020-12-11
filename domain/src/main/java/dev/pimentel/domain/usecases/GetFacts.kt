package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.repositories.FactsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetFacts(
    private val factsRepository: FactsRepository
) : UseCase<GetFacts.Params, List<Fact>> {

    override suspend fun invoke(params: Params): List<Fact> =
        factsRepository.getFacts(params.term).map { responseItem ->
            Fact(
                id = responseItem.id,
                category = responseItem.categories.firstOrNull(),
                url = responseItem.url,
                value = responseItem.value
            )
        }

    data class Params(
        val term: String
    )
}
