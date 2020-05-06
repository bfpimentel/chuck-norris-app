package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Completable

class SaveAllCategories(
    private val categoriesRepository: CategoriesRepository
) : UseCase<SaveAllCategories.Params, Completable> {

    override fun invoke(params: Params): Completable = Completable.create { emitter ->
        categoriesRepository.saveAllCategories(params.categoriesNames.map(::Category))
        emitter.onComplete()
    }

    data class Params(
        val categoriesNames: List<String>
    )
}
