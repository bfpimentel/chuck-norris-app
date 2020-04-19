package dev.pimentel.data.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.Category
import io.reactivex.Single

@Dao
interface CategoriesLocalDataSource {

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Single<List<Category>>

    @Insert
    fun saveAllCategories(categories: List<Category>)
}
