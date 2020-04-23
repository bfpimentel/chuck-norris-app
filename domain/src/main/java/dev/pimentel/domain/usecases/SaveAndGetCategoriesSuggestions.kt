package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class SaveAndGetCategoriesSuggestions(
    private val getAllCategoriesNames: GetAllCategoriesNames,
    private val saveAllCategories: SaveAllCategories,
    private val getCategorySuggestions: GetCategorySuggestions
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        getAllCategoriesNames(NoParams).flatMap { categoriesNames ->
            saveAllCategories(SaveAllCategories.Params(categoriesNames))
                .andThen(getCategorySuggestions(NoParams))
        }
}
