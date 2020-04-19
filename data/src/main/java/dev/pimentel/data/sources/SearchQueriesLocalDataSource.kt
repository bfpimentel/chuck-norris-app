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

    @Query("SELECT * FROM SearchQuery ORDER BY id LIMIT 10")
    fun fetchLastSearchQueries(): Single<List<SearchQuery>>

    @Query(
        """
        DELETE FROM SearchQuery
        WHERE id IN (
            SELECT id FROM SearchQuery
            ORDER BY id DESC
            LIMIT 1
        )
        """
    )
    fun deleteLastSearchQuery()
}
