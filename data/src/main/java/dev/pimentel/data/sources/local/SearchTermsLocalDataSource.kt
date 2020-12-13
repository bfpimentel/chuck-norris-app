package dev.pimentel.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pimentel.data.dto.SearchTermDTO

@Dao
internal interface SearchTermsLocalDataSource {

    @Query("SELECT * FROM SearchTermDTO ORDER BY id DESC LIMIT 1")
    suspend fun getSearchTerm(): SearchTermDTO

    @Query("SELECT id, term FROM SearchTermDTO where term = :term")
    suspend fun getSearchTermByTerm(term: String): List<SearchTermDTO>

    @Insert
    suspend fun insertSearchTerm(searchTerm: SearchTermDTO)

    @Query("DELETE FROM SearchTermDTO WHERE term = :term")
    suspend fun deleteSearchTermByTerm(term: String)

    @Query("SELECT * FROM SearchTermDTO ORDER BY id DESC")
    suspend fun getLastSearchTerms(): List<SearchTermDTO>

    @Query("SELECT COUNT(id) from SearchTermDTO")
    suspend fun getNumberOfSearchTerms(): Int

    @Query(
        """
        DELETE FROM SearchTermDTO
        WHERE id IN (
            SELECT id FROM SearchTermDTO
            ORDER BY id ASC
            LIMIT 1
        )
        """
    )
    suspend fun deleteLastSearchTerm()
}
