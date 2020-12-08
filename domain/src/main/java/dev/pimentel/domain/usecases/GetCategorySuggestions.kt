package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetCategorySuggestions(
    private val getAllCategories: GetAllCategories
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        getAllCategories(NoParams).map { savedCategories ->
            savedCategories.shuffled()
                .subList(fromIndex = FIRST_INDEX, toIndex = LAST_INDEX)
                .map(Category::name)
        }

    private companion object {
        const val FIRST_INDEX = 0
        const val LAST_INDEX = 8
    }
}
