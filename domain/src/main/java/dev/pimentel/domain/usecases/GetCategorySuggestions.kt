package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.CategorySuggestion
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetCategorySuggestions(
    private val fetchAllCategories: FetchAllCategories,
    private val fetchAllCategoriesNames: FetchAllCategoriesNames,
    private val saveAllCategories: SaveAllCategories
) : UseCase<NoParams, Single<List<CategorySuggestion>>> {

    override fun invoke(params: NoParams): Single<List<CategorySuggestion>> =
        fetchAllCategories(NoParams).flatMap { savedCategories ->
            if (savedCategories.isNotEmpty()) {
                Single.just(
                    savedCategories
                        .shuffled()
                        .subList(fromIndex = 0, toIndex = 7)
                        .map { CategorySuggestion(it.name) }
                )
            } else {
                fetchAllCategoriesNames(NoParams).flatMap { categoriesNames ->
                    saveAllCategories(SaveAllCategories.Params(categoriesNames))
                        .andThen(invoke(NoParams))
                }
            }
        }
}
