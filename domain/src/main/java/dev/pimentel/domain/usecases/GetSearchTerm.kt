package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class GetSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, String> {

    override suspend fun invoke(params: NoParams): String =
        try {
            searchTermsRepository.getSearchTerm().term
        } catch (exception: Exception) {
            throw SearchTermNotFoundException
        }

    object SearchTermNotFoundException : Exception()
}
