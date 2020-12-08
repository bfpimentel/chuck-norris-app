package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class SaveNonExistingSearchTerm(
    private val areSearchTermsOnLimit: AreSearchTermsOnLimit,
    private val deleteLastSearchTerm: DeleteLastSearchTerm,
    private val saveSearchTerm: SaveSearchTerm
) : UseCase<SaveNonExistingSearchTerm.Params, Unit> {

    override suspend fun invoke(params: Params) {
        val onLimit = areSearchTermsOnLimit(NoParams)
        if (onLimit) deleteLastSearchTerm(NoParams)
        saveSearchTerm(SaveSearchTerm.Params(params.term))
    }

    data class Params(
        val term: String
    )
}
