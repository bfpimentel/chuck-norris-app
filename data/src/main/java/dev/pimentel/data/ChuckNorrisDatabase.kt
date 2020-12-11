package dev.pimentel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.pimentel.data.dto.CategoryDTO
import dev.pimentel.data.dto.SearchTermDTO
import dev.pimentel.data.sources.local.CategoriesLocalDataSource
import dev.pimentel.data.sources.local.SearchTermsLocalDataSource

@Database(
    entities = [
        CategoryDTO::class,
        SearchTermDTO::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ChuckNorrisDatabase : RoomDatabase() {

    abstract fun categoriesLocalDataSource(): CategoriesLocalDataSource

    abstract fun searchTermsLocalDataSource(): SearchTermsLocalDataSource
}
