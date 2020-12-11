package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class GetAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<NoParams, List<String>> {

    override suspend fun invoke(params: NoParams): List<String> =
        categoriesRepository.getAllCategories()
}
