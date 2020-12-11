package dev.pimentel.data.repository

import dev.pimentel.data.dto.SearchTermDTO
import dev.pimentel.data.repositories.SearchTermsRepositoryImpl
import dev.pimentel.data.sources.local.SearchTermsLocalDataSource
import dev.pimentel.domain.repositories.SearchTermsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchTermsRepositoryTest {

    private val localDataSource = mockk<SearchTermsLocalDataSource>()
    private lateinit var searchTermsRepository: SearchTermsRepository

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        searchTermsRepository = SearchTermsRepositoryImpl(localDataSource)

        assertNotNull(searchTermsRepository)
    }

    @Test
    fun `should route getSearchTerm call to localDataSource`() = runBlocking {
        val searchTerm = SearchTermDTO(0, "term")

        coEvery { localDataSource.getSearchTerm() } returns searchTerm

        assertEquals(searchTermsRepository.getSearchTerm(), "term")

        coVerify(exactly = 1) { localDataSource.getSearchTerm() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getSearchTermsByTerm call to localDataSource`() = runBlocking {
        val term = "term"
        val searchTerms = listOf(
            SearchTermDTO(1, "term1"),
            SearchTermDTO(2, "term2")
        )
        val result = listOf(
            "term1",
            "term2",
        )

        coEvery { localDataSource.getSearchTermByTerm(term) } returns searchTerms

        assertEquals(searchTermsRepository.getSearchTermByTerm(term), result)

        coVerify(exactly = 1) { localDataSource.getSearchTermByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getLastSearchTerms call to localDataSource`() = runBlocking {
        val searchTerms = listOf(
            SearchTermDTO(1, "term1"),
            SearchTermDTO(2, "term2")
        )
        val result = listOf(
            "term1",
            "term2",
        )

        coEvery { localDataSource.getLastSearchTerms() } returns searchTerms

        assertEquals(searchTermsRepository.getLastSearchTerms(), result)

        coVerify(exactly = 1) { localDataSource.getLastSearchTerms() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route insertSearchTerm call to localDataSource`() = runBlocking {
        val searchTerm = SearchTermDTO(0, "term")

        coEvery { localDataSource.insertSearchTerm(searchTerm) } just runs

        searchTermsRepository.saveSearchTerm("term")

        coVerify(exactly = 1) { localDataSource.insertSearchTerm(searchTerm) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteSearchTermByTerm call to localDataSource`() = runBlocking {
        val term = "term"

        coEvery { localDataSource.deleteSearchTermByTerm(term) } just runs

        searchTermsRepository.deleteSearchTermByTerm("term")

        coVerify(exactly = 1) { localDataSource.deleteSearchTermByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getNumberOfSearchTerms call to localDataSource`() = runBlocking {
        val numberOfSearchTerms = 10

        coEvery { localDataSource.getNumberOfSearchTerms() } returns numberOfSearchTerms

        assertEquals(searchTermsRepository.getNumberOfSearchTerms(), numberOfSearchTerms)

        coVerify(exactly = 1) { localDataSource.getNumberOfSearchTerms() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteLastSearchTerm call to localDataSource`() = runBlocking {
        coEvery { localDataSource.deleteLastSearchTerm() } just runs

        searchTermsRepository.deleteLastSearchTerm()

        coVerify(exactly = 1) { localDataSource.deleteLastSearchTerm() }
        confirmVerified(localDataSource)
    }
}
