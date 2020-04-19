package dev.pimentel.domain.usecases

import dev.pimentel.domain.DeleteSearchTerm
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveExistingSearchTerm(
    private val deleteSearchTerm: DeleteSearchTerm,
    private val saveSearchTerm: SaveSearchTerm
) : UseCase<SaveExistingSearchTerm.Params, Completable> {

    override fun invoke(params: Params): Completable =
        deleteSearchTerm(DeleteSearchTerm.Params(params.term))
            .andThen(saveSearchTerm(SaveSearchTerm.Params(params.term)))

    data class Params(
        val term: String
    )
}
