package dev.pimentel.data.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.Category
import io.reactivex.Single

@Dao
interface CategoriesLocalDataSource {

    @Query("SELECT * FROM Category")
    fun fetchAllCategories(): Single<List<Category>>

    @Insert
    fun insertAllCategories(categories: List<Category>)
}
