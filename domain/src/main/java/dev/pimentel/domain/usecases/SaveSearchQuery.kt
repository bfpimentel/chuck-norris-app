package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchQuery
import dev.pimentel.data.repositories.SearchQueriesRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveSearchQuery(
    private val searchQueriesRepository: SearchQueriesRepository
) : UseCase<SaveSearchQuery.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        searchQueriesRepository.insertSearchQuery(params.searchQuery)
        emitter.onComplete()
    }

    data class Params(
        val searchQuery: SearchQuery
    )
}
