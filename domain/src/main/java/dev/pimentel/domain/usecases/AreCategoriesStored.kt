package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class AreCategoriesStored(
    private val getAllCategories: GetAllCategories
) : UseCase<NoParams, Single<Boolean>> {

    override fun invoke(params: NoParams): Single<Boolean> =
        getAllCategories(NoParams).map(List<Category>::isNotEmpty)
}
