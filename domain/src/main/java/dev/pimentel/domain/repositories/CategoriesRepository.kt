package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.Category
import io.reactivex.Single

interface CategoriesRepository {
    fun getAllCategories(): Single<List<Category>>
    fun getAllCategoriesNames(): Single<List<String>>
    fun saveAllCategories(categories: List<Category>)
}
