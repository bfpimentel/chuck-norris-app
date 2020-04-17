package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<SaveAllCategories.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        categoriesRepository.saveAllCategories(params.categories)
        emitter.onComplete()
    }

    data class Params(
        val categories: List<Category>
    )
}
