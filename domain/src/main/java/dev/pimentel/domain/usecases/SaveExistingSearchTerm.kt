package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.UseCase

class SaveExistingSearchTerm(
    private val deleteSearchTerm: DeleteSearchTerm,
    private val saveSearchTerm: SaveSearchTerm
) : UseCase<SaveExistingSearchTerm.Params, Unit> {

    override suspend fun invoke(params: Params) {
        deleteSearchTerm(DeleteSearchTerm.Params(params.term))
        saveSearchTerm(SaveSearchTerm.Params(params.term))
    }

    data class Params(
        val term: String
    )
}
