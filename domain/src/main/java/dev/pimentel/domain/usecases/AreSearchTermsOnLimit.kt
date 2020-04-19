package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class AreSearchTermsOnLimit(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Single<Boolean>> {

    override fun invoke(params: NoParams): Single<Boolean> =
        searchTermsRepository.getNumberOfSearchTerms().map { numberOfSearchQueries ->
            numberOfSearchQueries >= MAX_NUMBER_OF_SEARCH_QUERIES
        }

    private companion object {
        const val MAX_NUMBER_OF_SEARCH_QUERIES = 10
    }
}
