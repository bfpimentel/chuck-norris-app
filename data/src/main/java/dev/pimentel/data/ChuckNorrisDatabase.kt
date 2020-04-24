package dev.pimentel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.pimentel.data.models.Category
import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.sources.CategoriesLocalDataSource
import dev.pimentel.data.sources.SearchTermsLocalDataSource

@Database(
    entities = [
        Category::class,
        SearchTerm::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ChuckNorrisDatabase : RoomDatabase() {

    abstract fun categoriesLocalDataSource(): CategoriesLocalDataSource

    abstract fun searchTermsLocalDataSource(): SearchTermsLocalDataSource
}
