package dev.pimentel.data.sources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.SearchQuery
import io.reactivex.Single

@Dao
interface SearchQueriesLocalDataSource {

    @Insert
    fun insertSearchQuery(searchQuery: SearchQuery)

    @Delete
    fun deleteSearchQuery(searchQuery: SearchQuery)

    @Query("SELECT * FROM SearchQuery LIMIT 10")
    fun fetchLastSearchQueries(): Single<List<SearchQuery>>
}
