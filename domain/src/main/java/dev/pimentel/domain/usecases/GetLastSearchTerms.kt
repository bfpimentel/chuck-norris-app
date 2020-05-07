package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.SearchTerm
import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetLastSearchTerms(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        searchTermsRepository.getLastSearchTerms().map { lastTerms ->
            lastTerms.map(SearchTerm::term)
        }
}
