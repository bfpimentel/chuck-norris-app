package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class DoesSearchTermExist(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<DoesSearchTermExist.Params, Boolean> {

    override suspend fun invoke(params: Params): Boolean =
        searchTermsRepository.getSearchTermByTerm(params.term).isNotEmpty()

    data class Params(
        val term: String
    )
}
