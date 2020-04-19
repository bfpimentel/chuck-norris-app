package dev.pimentel.domain.usecases

import android.content.Context
import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.R
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetFacts(
    private val getSearchTerm: GetSearchTerm,
    private val factsRepository: FactsRepository,
    private val context: Context
) : UseCase<NoParams, Single<List<Fact>>> {

    override fun invoke(params: NoParams): Single<List<Fact>> =
        getSearchTerm(NoParams).flatMap { searchTerm ->
            factsRepository.getFacts(searchTerm).map { response ->
                response.result.map { responseItem ->
                    Fact(
                        responseItem.categories
                            .firstOrNull()
                            ?: context.getString(R.string.get_facts_no_category),
                        responseItem.url,
                        responseItem.value
                    )
                }
            }
        }
}
