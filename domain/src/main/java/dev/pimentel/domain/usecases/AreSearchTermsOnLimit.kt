package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class AreSearchTermsOnLimit(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Boolean> {

    override suspend fun invoke(params: NoParams): Boolean =
        searchTermsRepository.getNumberOfSearchTerms() >= MAX_NUMBER_OF_SEARCH_TERMS

    private companion object {
        const val MAX_NUMBER_OF_SEARCH_TERMS = 10
    }
}
