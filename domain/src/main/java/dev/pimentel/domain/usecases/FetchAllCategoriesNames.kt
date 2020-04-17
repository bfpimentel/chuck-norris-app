package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.Single

class FetchAllCategoriesNames(
    private val categoriesRepository: CategoriesRepository
) : UseCase<NoParams, Single<List<String>>> {

    override fun invoke(params: NoParams): Single<List<String>> =
        categoriesRepository.fetchAllCategoriesNames()
}
