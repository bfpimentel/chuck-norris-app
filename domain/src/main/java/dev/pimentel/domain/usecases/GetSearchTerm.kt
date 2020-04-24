package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Single<String>> {

    override fun invoke(params: NoParams): Single<String> =
        searchTermsRepository.getSearchTerm()
            .map(SearchTerm::term)
            .onErrorResumeNext { Single.error(SearchTermNotFoundException()) }

    class SearchTermNotFoundException : Exception()
}
