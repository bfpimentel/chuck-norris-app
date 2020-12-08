package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.SearchTerm
import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class SaveSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<SaveSearchTerm.Params, Unit> {

    override suspend fun invoke(params: Params) =
        searchTermsRepository.saveSearchTerm(SearchTerm(term = params.term))

    data class Params(
        val term: String
    )
}
