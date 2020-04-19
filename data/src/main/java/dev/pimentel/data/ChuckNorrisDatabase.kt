package dev.pimentel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.pimentel.data.models.Category
import dev.pimentel.data.models.SearchQuery
import dev.pimentel.data.sources.CategoriesLocalDataSource
import dev.pimentel.data.sources.SearchQueriesLocalDataSource

@Database(entities = [Category::class, SearchQuery::class], version = 1)
abstract class ChuckNorrisDatabase : RoomDatabase() {

    abstract fun categoriesLocalDataSource(): CategoriesLocalDataSource

    abstract fun searchQueriesLocalDataSource(): SearchQueriesLocalDataSource
}
