package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetFacts(
    private val getSearchTerm: GetSearchTerm,
    private val factsRepository: FactsRepository
) : UseCase<NoParams, Single<List<Fact>>> {

    override fun invoke(params: NoParams): Single<List<Fact>> =
        getSearchTerm(NoParams).flatMap { searchTerm ->
            factsRepository.getFacts(searchTerm).map { response ->
                response.map { responseItem ->
                    Fact(
                        responseItem.categories,
                        responseItem.url,
                        responseItem.value
                    )
                }
            }
        }
}
