package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class DeleteSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<DeleteSearchTerm.Params, Unit> {

    override suspend fun invoke(params: Params) =
        searchTermsRepository.deleteSearchTermByTerm(params.term)

    data class Params(
        val term: String
    )
}
