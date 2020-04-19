package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class HandleSearchTermSaving(
    private val doesSearchTermExist: DoesSearchTermExist,
    private val saveExistingSearchTerm: SaveExistingSearchTerm,
    private val saveNonExistingSearchTerm: SaveNonExistingSearchTerm
) : UseCase<HandleSearchTermSaving.Params, Completable> {

    override fun invoke(params: Params): Completable =
        doesSearchTermExist(DoesSearchTermExist.Params(params.term))
            .flatMapCompletable { exists ->
                if (exists) saveExistingSearchTerm(SaveExistingSearchTerm.Params(params.term))
                else saveNonExistingSearchTerm(SaveNonExistingSearchTerm.Params(params.term))
            }

    data class Params(
        val term: String
    )
}
