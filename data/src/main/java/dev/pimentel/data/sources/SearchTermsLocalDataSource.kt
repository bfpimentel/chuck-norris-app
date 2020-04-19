package dev.pimentel.data.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.SearchTerm
import io.reactivex.Single

@Dao
interface SearchTermsLocalDataSource {

    @Query("SELECT id, term FROM SearchTerm where term = :term")
    fun fetchSearchTermsByTerm(term: String): Single<List<SearchTerm>>

    @Insert
    fun insertSearchTerm(searchTerm: SearchTerm)

    @Query("DELETE FROM SearchTerm WHERE term = :term")
    fun deleteSearchTermByTerm(term: String)

    @Query("SELECT * FROM SearchTerm")
    fun fetchLastSearchTerms(): Single<List<SearchTerm>>

    @Query("SELECT COUNT(id) from SearchTerm")
    fun getNumberOfSearchTerms(): Single<Int>

    @Query(
        """
        DELETE FROM SearchTerm
        WHERE id IN (
            SELECT id FROM SearchTerm
            ORDER BY id ASC
            LIMIT 1
        )
        """
    )
    fun deleteLastSearchTerm()
}
