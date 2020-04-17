package dev.pimentel.data.repositories

import dev.pimentel.data.models.Category
import dev.pimentel.data.sources.CategoriesLocalDataSource
import dev.pimentel.data.sources.CategoriesRemoteDataSource
import io.reactivex.Single

interface CategoriesRepository {
    fun fetchAllCategories(): Single<List<Category>>
    fun fetchAllCategoriesNames(): Single<List<String>>
    fun saveAllCategories(categories: List<Category>)
}

class CategoriesRepositoryImpl(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {

    override fun fetchAllCategories(): Single<List<Category>> =
        localDataSource.fetchAllCategories()

    override fun fetchAllCategoriesNames(): Single<List<String>> =
        remoteDataSource.fetchAllCategoriesNames()

    override fun saveAllCategories(categories: List<Category>) =
        localDataSource.insertAllCategories(categories)
}
