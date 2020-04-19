package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class DeleteLastSearchTerm(
    private val searchTermsRepository: SearchTermsRepository
) : UseCase<NoParams, Completable> {

    override fun invoke(params: NoParams): Completable = Completable.create { emitter ->
        searchTermsRepository.deleteLastSearchTerm()
        emitter.onComplete()
    }
}
