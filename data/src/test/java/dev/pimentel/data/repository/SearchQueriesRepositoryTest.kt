package dev.pimentel.data.repository

import dev.pimentel.data.models.SearchQuery
import dev.pimentel.data.repositories.SearchQueriesRepository
import dev.pimentel.data.repositories.SearchQueriesRepositoryImpl
import dev.pimentel.data.sources.SearchQueriesLocalDataSource
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

class SearchQueriesRepositoryTest {

    private val localDataSource = mockk<SearchQueriesLocalDataSource>()
    private lateinit var searchQueriesRepository: SearchQueriesRepository

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        searchQueriesRepository = SearchQueriesRepositoryImpl(localDataSource)

        assertNotNull(searchQueriesRepository)
    }

    @Test
    fun `should route fetchLastSearchQueries call to localDataSource`() {
        val searchQueries = listOf(
            SearchQuery(1, "query1"),
            SearchQuery(2, "query2")
        )

        every { localDataSource.fetchLastSearchQueries() } returns Single.just(searchQueries)

        searchQueriesRepository.fetchLastSearchQueries()
            .test()
            .assertNoErrors()
            .assertResult(searchQueries)

        verify(exactly = 1) { localDataSource.fetchLastSearchQueries() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route insertSearchQuery call to localDataSource`() {
        val searchQuery = SearchQuery(1, "query")

        every { localDataSource.insertSearchQuery(searchQuery) } just runs

        searchQueriesRepository.insertSearchQuery(searchQuery)

        verify(exactly = 1) { localDataSource.insertSearchQuery(searchQuery) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteSearchQuery call to localDataSource`() {
        val searchQuery = SearchQuery(1, "query")

        every { localDataSource.deleteSearchQuery(searchQuery) } just runs

        searchQueriesRepository.deleteSearchQuery(searchQuery)

        verify(exactly = 1) { localDataSource.deleteSearchQuery(searchQuery) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteLastSearchQuery call to localDataSource`() {
        every { localDataSource.deleteLastSearchQuery() } just runs

        searchQueriesRepository.deleteLastSearchQuery()

        verify(exactly = 1) { localDataSource.deleteLastSearchQuery() }
        confirmVerified(localDataSource)
    }
}
