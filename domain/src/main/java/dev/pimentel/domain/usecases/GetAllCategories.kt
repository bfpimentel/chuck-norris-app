package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<NoParams, Single<List<Category>>> {

    override fun invoke(params: NoParams): Single<List<Category>> =
        categoriesRepository.getAllCategories()
}
