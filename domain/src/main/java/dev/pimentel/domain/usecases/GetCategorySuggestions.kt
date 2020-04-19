package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetCategorySuggestions(
    private val getAllCategories: GetAllCategories,
    private val getAllCategoriesNames: GetAllCategoriesNames,
    private val saveAllCategories: SaveAllCategories,
    private val shuffleList: ShuffleList
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        getAllCategories(NoParams).flatMap { savedCategories ->
            if (savedCategories.isNotEmpty()) {
                Single.just(
                    shuffleList(ShuffleList.Params(savedCategories))
                        .subList(fromIndex = 0, toIndex = 8)
                        .map(Category::name)
                )
            } else {
                getAllCategoriesNames(NoParams).flatMap { categoriesNames ->
                    saveAllCategories(SaveAllCategories.Params(categoriesNames))
                        .andThen(invoke(NoParams))
                }
            }
        }
}
