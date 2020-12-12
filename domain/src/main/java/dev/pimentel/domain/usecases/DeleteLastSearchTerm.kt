package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class DeleteLastSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Unit> {

    override suspend fun invoke(params: NoParams) =
        searchTermsRepository.deleteLastSearchTerm()
}
