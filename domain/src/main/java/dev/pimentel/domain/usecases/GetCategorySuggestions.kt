package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetCategorySuggestions(
    private val getAllCategories: GetAllCategories,
    private val shuffleList: ShuffleList
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        getAllCategories(NoParams).map { savedCategories ->
            shuffleList(ShuffleList.Params(savedCategories))
                .subList(fromIndex = 0, toIndex = 8)
                .map(Category::name)
        }
}
