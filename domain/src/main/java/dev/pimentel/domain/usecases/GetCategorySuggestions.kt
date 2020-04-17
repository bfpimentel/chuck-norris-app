package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.CategorySuggestion
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetCategorySuggestions(
    private val fetchAllCategories: FetchAllCategories,
    private val fetchAllCategoriesNames: FetchAllCategoriesNames,
    private val saveAllCategories: SaveAllCategories,
    private val shuffleList: ShuffleList
) : UseCase<NoParams, Single<List<CategorySuggestion>>> {

    override fun invoke(params: NoParams): Single<List<CategorySuggestion>> =
        fetchAllCategories(NoParams).flatMap { savedCategories ->
            if (savedCategories.isNotEmpty()) {
                Single.just(
                    shuffleList(ShuffleList.Params(savedCategories))
                        .subList(fromIndex = 0, toIndex = 8)
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
