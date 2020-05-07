package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class GetAllCategoriesNames(
    private val categoriesRepository: CategoriesRepository
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        categoriesRepository.getAllCategoriesNames()
}
