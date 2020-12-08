package dev.pimentel.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.Category

@Dao
interface CategoriesLocalDataSource {

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Insert
    suspend fun saveAllCategories(categories: List<Category>)
}
