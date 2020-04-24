package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class DeleteSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<DeleteSearchTerm.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        searchTermsRepository.deleteSearchTermByTerm(params.term)
        emitter.onComplete()
    }

    data class Params(
        val term: String
    )
}
