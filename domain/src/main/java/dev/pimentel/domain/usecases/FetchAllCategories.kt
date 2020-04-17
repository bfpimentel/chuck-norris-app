package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class FetchAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<NoParams, Single<List<Category>>> {

    override fun invoke(params: NoParams): Single<List<Category>> =
        categoriesRepository.fetchAllCategories()
}
