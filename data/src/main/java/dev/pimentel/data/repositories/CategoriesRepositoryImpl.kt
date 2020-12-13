package dev.pimentel.data.repositories

import dev.pimentel.data.dto.CategoryDTO
import dev.pimentel.data.sources.local.CategoriesLocalDataSource
import dev.pimentel.data.sources.remote.CategoriesRemoteDataSource
import dev.pimentel.domain.repositories.CategoriesRepository

internal class CategoriesRepositoryImpl(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {

    override suspend fun getAllCategories(): List<String> =
        localDataSource.getAllCategories().map(CategoryDTO::name)

    override suspend fun getAllCategoriesNames(): List<String> =
        remoteDataSource.getAllCategoriesNames()

    override suspend fun saveAllCategories(categories: List<String>) =
        localDataSource.saveAllCategories(categories = categories.map(::CategoryDTO))
}
