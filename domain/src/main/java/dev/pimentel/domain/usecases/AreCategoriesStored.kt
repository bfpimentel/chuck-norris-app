package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class AreCategoriesStored(
    private val getAllCategories: GetAllCategories
) : UseCase<NoParams, Boolean> {

    override suspend fun invoke(params: NoParams): Boolean =
        getAllCategories(NoParams).isNotEmpty()
}
