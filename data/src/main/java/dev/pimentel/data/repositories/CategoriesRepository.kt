package dev.pimentel.data.repositories

import dev.pimentel.data.models.Category
import dev.pimentel.data.sources.CategoriesLocalDataSource
import dev.pimentel.data.sources.CategoriesRemoteDataSource
import io.reactivex.Single

interface CategoriesRepository {
    fun getAllCategories(): Single<List<Category>>
    fun getAllCategoriesNames(): Single<List<String>>
    fun saveAllCategories(categories: List<Category>)
}

internal class CategoriesRepositoryImpl(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {

    override fun getAllCategories(): Single<List<Category>> =
        localDataSource.getAllCategories()

    override fun getAllCategoriesNames(): Single<List<String>> =
        remoteDataSource.getAllCategoriesNames()

    override fun saveAllCategories(categories: List<Category>) =
        localDataSource.saveAllCategories(categories)
}
