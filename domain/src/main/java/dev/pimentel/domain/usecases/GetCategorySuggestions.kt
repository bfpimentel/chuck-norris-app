package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase

class GetCategorySuggestions(
    private val getAllCategories: GetAllCategories
) : UseCase<NoParams, List<String>> {

    override suspend fun invoke(params: NoParams): List<String> =
        getAllCategories(NoParams)
            .shuffled()
            .let { shuffledCategories ->
                if (shuffledCategories.size <= MAX_ITEMS) shuffledCategories
                else shuffledCategories.subList(fromIndex = FIRST_INDEX, toIndex = LAST_INDEX)
            }

    private companion object {
        const val MAX_ITEMS = 8

        const val FIRST_INDEX = 0
        const val LAST_INDEX = 8
    }
}
