package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.UseCase

class HandleSearchTermSaving(
    private val doesSearchTermExist: DoesSearchTermExist,
    private val saveExistingSearchTerm: SaveExistingSearchTerm,
    private val saveNonExistingSearchTerm: SaveNonExistingSearchTerm
) : UseCase<HandleSearchTermSaving.Params, Unit> {

    override suspend fun invoke(params: Params) {
        val exists = doesSearchTermExist(DoesSearchTermExist.Params(params.term))
        if (exists) saveExistingSearchTerm(SaveExistingSearchTerm.Params(params.term))
        else saveNonExistingSearchTerm(SaveNonExistingSearchTerm.Params(params.term))
    }

    data class Params(
        val term: String
    )
}
