package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.Category

interface CategoriesRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getAllCategoriesNames(): List<String>
    suspend fun saveAllCategories(categories: List<Category>)
}
