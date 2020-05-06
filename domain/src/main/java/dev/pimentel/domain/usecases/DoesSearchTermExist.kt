package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class DoesSearchTermExist(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<DoesSearchTermExist.Params, Single<Boolean>> {

    override fun invoke(params: Params): Single<Boolean> =
        searchTermsRepository.getSearchTermByTerm(params.term).map { searchTerms ->
            searchTerms.isNotEmpty()
        }

    data class Params(
        val term: String
    )
}
