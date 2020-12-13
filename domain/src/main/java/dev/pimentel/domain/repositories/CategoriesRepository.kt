package dev.pimentel.domain.repositories

interface CategoriesRepository {
    suspend fun getAllCategories(): List<String>
    suspend fun getAllCategoriesNames(): List<String>
    suspend fun saveAllCategories(categories: List<String>)
}
