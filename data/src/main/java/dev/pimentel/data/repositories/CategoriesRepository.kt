package dev.pimentel.data.repositories

import dev.pimentel.data.models.Category
import dev.pimentel.data.sources.local.CategoriesLocalDataSource
import dev.pimentel.data.sources.remote.CategoriesRemoteDataSource
import dev.pimentel.domain.repositories.CategoriesRepository
import io.reactivex.Single
import dev.pimentel.domain.models.Category as DomainCategory

internal class CategoriesRepositoryImpl(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {

    override fun getAllCategories(): Single<List<DomainCategory>> =
        localDataSource.getAllCategories()
            .map { categories ->
                categories.map { category -> DomainCategory(category.name) }
            }

    override fun getAllCategoriesNames(): Single<List<String>> =
        remoteDataSource.getAllCategoriesNames()

    override fun saveAllCategories(categories: List<DomainCategory>) =
        localDataSource.saveAllCategories(
            categories.map { category -> Category(category.name) }
        )
}
