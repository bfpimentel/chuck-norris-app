package dev.pimentel.data.repositories

import dev.pimentel.data.models.Category
import dev.pimentel.data.sources.local.CategoriesLocalDataSource
import dev.pimentel.data.sources.remote.CategoriesRemoteDataSource
import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.models.Category as DomainCategory

internal class CategoriesRepositoryImpl(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {

    override suspend fun getAllCategories(): List<DomainCategory> =
        localDataSource.getAllCategories()
            .map { category -> DomainCategory(category.name) }

    override suspend fun getAllCategoriesNames(): List<String> =
        remoteDataSource.getAllCategoriesNames()

    override suspend fun saveAllCategories(categories: List<DomainCategory>) =
        localDataSource.saveAllCategories(
            categories.map { category -> Category(category.name) }
        )
}
