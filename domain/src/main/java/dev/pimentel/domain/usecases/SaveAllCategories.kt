package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.UseCase

class SaveAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<SaveAllCategories.Params, Unit> {

    override suspend fun invoke(params: Params) =
        categoriesRepository.saveAllCategories(params.categoriesNames)

    data class Params(
        val categoriesNames: List<String>
    )
}
