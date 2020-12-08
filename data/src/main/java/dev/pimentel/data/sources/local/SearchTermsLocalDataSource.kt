package dev.pimentel.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.models.SearchTerm

@Dao
interface SearchTermsLocalDataSource {

    @Query("SELECT * FROM SearchTerm ORDER BY id DESC LIMIT 1")
    suspend fun getSearchTerm(): SearchTerm

    @Query("SELECT id, term FROM SearchTerm where term = :term")
    suspend fun getSearchTermByTerm(term: String): List<SearchTerm>

    @Insert
    suspend fun insertSearchTerm(searchTerm: SearchTerm)

    @Query("DELETE FROM SearchTerm WHERE term = :term")
    suspend fun deleteSearchTermByTerm(term: String)

    @Query("SELECT * FROM SearchTerm ORDER BY id DESC")
    suspend fun getLastSearchTerms(): List<SearchTerm>

    @Query("SELECT COUNT(id) from SearchTerm")
    suspend fun getNumberOfSearchTerms(): Int

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
    suspend fun deleteLastSearchTerm()
}
