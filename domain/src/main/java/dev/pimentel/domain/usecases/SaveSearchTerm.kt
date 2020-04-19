package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<SaveSearchTerm.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        searchTermsRepository.insertSearchTerm(SearchTerm(term = params.query))
        emitter.onComplete()
    }

    data class Params(
        val query: String
    )
}
