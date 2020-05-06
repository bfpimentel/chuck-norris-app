package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.repositories.FactsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetFacts(
    private val factsRepository: FactsRepository
) : UseCase<GetFacts.Params, Single<List<Fact>>> {

    override fun invoke(params: Params): Single<List<Fact>> =
        factsRepository.getFacts(params.term).map { response ->
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
