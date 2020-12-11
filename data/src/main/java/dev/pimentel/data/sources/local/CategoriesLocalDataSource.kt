package dev.pimentel.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.dto.CategoryDTO

@Dao
interface CategoriesLocalDataSource {

    @Query("SELECT * FROM CategoryDTO")
    suspend fun getAllCategories(): List<CategoryDTO>

    @Insert
    suspend fun saveAllCategories(categories: List<CategoryDTO>)
}
