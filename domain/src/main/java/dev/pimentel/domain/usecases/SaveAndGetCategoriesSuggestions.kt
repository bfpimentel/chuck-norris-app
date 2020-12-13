package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class SaveAndGetCategoriesSuggestions(
    private val getAllCategoriesNames: GetAllCategoriesNames,
    private val saveAllCategories: SaveAllCategories,
    private val getCategorySuggestions: GetCategorySuggestions
) : UseCase<NoParams, List<String>> {

    override suspend fun invoke(params: NoParams): List<String> {
        val categoriesNames = getAllCategoriesNames(NoParams)
        saveAllCategories(SaveAllCategories.Params(categoriesNames))
        return getCategorySuggestions(NoParams)
    }
}
