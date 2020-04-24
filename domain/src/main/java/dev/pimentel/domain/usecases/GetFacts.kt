package dev.pimentel.domain.usecases

import android.content.Context
import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.R
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetFacts(
    private val factsRepository: FactsRepository,
    private val context: Context
) : UseCase<GetFacts.Params, Single<List<Fact>>> {

    override fun invoke(params: Params): Single<List<Fact>> =
        factsRepository.getFacts(params.term).map { response ->
            response.result.map { responseItem ->
                Fact(
                    responseItem.id,
                    responseItem.categories
                        .firstOrNull()
                        ?: context.getString(R.string.get_facts_no_category),
                    responseItem.url,
                    responseItem.value
                )
            }
        }

    data class Params(
        val term: String
    )
}
