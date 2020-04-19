package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveNonExistingSearchTerm(
    private val areSearchTermsOnLimit: AreSearchTermsOnLimit,
    private val deleteLastSearchTerm: DeleteLastSearchTerm,
    private val saveSearchTerm: SaveSearchTerm
) : UseCase<SaveNonExistingSearchTerm.Params, Completable> {

    override fun invoke(params: Params): Completable =
        areSearchTermsOnLimit(NoParams).flatMapCompletable { onLimit ->
            if (onLimit) {
                deleteLastSearchTerm(NoParams)
                    .andThen(saveSearchTerm(SaveSearchTerm.Params(params.query)))
            } else {
                saveSearchTerm(SaveSearchTerm.Params(params.query))
            }
        }

    data class Params(
        val query: String
    )
}
