package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class GetLastSearchTerms(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, List<String>> {

    override suspend fun invoke(params: NoParams): List<String> =
        searchTermsRepository.getLastSearchTerms()
}
