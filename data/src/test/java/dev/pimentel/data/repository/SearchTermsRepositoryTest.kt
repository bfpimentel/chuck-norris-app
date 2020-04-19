package dev.pimentel.data.repository

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.data.repositories.SearchTermsRepositoryImpl
import dev.pimentel.data.sources.SearchTermsLocalDataSource
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertNotNull
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
    fun `should route fetchSearchTermsByTerm call to localDataSource`() {
        val term = "term"
        val searchTerms = listOf(
            SearchTerm(1, "query1"),
            SearchTerm(2, "query2")
        )

        every { localDataSource.fetchSearchTermsByTerm(term) } returns Single.just(searchTerms)

        searchTermsRepository.fetchSearchTermByTerm(term)
            .test()
            .assertNoErrors()
            .assertResult(searchTerms)

        verify(exactly = 1) { localDataSource.fetchSearchTermsByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route fetchLastSearchTerms call to localDataSource`() {
        val searchTerms = listOf(
            SearchTerm(1, "query1"),
            SearchTerm(2, "query2")
        )

        every { localDataSource.fetchLastSearchTerms() } returns Single.just(searchTerms)

        searchTermsRepository.fetchLastSearchTerms()
            .test()
            .assertNoErrors()
            .assertResult(searchTerms)

        verify(exactly = 1) { localDataSource.fetchLastSearchTerms() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route insertSearchTerm call to localDataSource`() {
        val searchTerm = SearchTerm(1, "query")

        every { localDataSource.insertSearchTerm(searchTerm) } just runs

        searchTermsRepository.insertSearchTerm(searchTerm)

        verify(exactly = 1) { localDataSource.insertSearchTerm(searchTerm) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteSearchTermByTerm call to localDataSource`() {
        val term = "term"

        every { localDataSource.deleteSearchTermByTerm(term) } just runs

        searchTermsRepository.deleteSearchTermByTerm(term)

        verify(exactly = 1) { localDataSource.deleteSearchTermByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getNumberOfSearchTerms call to localDataSource`() {
        val numberOfSearchTerms = 10

        every { localDataSource.getNumberOfSearchTerms() } returns Single.just(numberOfSearchTerms)

        searchTermsRepository.getNumberOfSearchTerms()
            .test()
            .assertNoErrors()
            .assertResult(numberOfSearchTerms)

        verify(exactly = 1) { localDataSource.getNumberOfSearchTerms() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteLastSearchTerm call to localDataSource`() {
        every { localDataSource.deleteLastSearchTerm() } just runs

        searchTermsRepository.deleteLastSearchTerm()

        verify(exactly = 1) { localDataSource.deleteLastSearchTerm() }
        confirmVerified(localDataSource)
    }
}
