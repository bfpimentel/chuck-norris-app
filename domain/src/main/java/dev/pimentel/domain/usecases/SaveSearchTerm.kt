package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.SearchTerm
import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<SaveSearchTerm.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        searchTermsRepository.saveSearchTerm(SearchTerm(term = params.term))
        emitter.onComplete()
    }

    data class Params(
        val term: String
    )
}
