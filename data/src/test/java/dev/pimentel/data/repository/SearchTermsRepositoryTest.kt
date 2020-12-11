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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.entities.SearchTerm as DomainSearchTerm

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
        val domainSearchTerm = DomainSearchTerm("term")

        coEvery { localDataSource.getSearchTerm() } returns searchTerm

        searchTermsRepository.getSearchTerm()
            .test()
            .assertNoErrors()
            .assertResult(domainSearchTerm)

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
        val domainSearchTerms = listOf(
            DomainSearchTerm("term1"),
            DomainSearchTerm("term2")
        )

        coEvery { localDataSource.getSearchTermByTerm(term) } returns searchTerms

        searchTermsRepository.getSearchTermByTerm(term)
            .test()
            .assertNoErrors()
            .assertResult(domainSearchTerms)

        coVerify(exactly = 1) { localDataSource.getSearchTermByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getLastSearchTerms call to localDataSource`() = runBlocking {
        val searchTerms = listOf(
            SearchTermDTO(1, "term1"),
            SearchTermDTO(2, "term2")
        )
        val domainSearchTerms = listOf(
            DomainSearchTerm("term1"),
            DomainSearchTerm("term2")
        )

        coEvery { localDataSource.getLastSearchTerms() } returns searchTerms

        searchTermsRepository.getLastSearchTerms()
            .test()
            .assertNoErrors()
            .assertResult(domainSearchTerms)

        coVerify(exactly = 1) { localDataSource.getLastSearchTerms() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route insertSearchTerm call to localDataSource`() = runBlocking {
        val searchTerm = SearchTermDTO(0, "term")
        val domainSearchTerm = DomainSearchTerm("term")

        coEvery { localDataSource.insertSearchTerm(searchTerm) } just runs

        searchTermsRepository.saveSearchTerm(domainSearchTerm)

        coVerify(exactly = 1) { localDataSource.insertSearchTerm(searchTerm) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route deleteSearchTermByTerm call to localDataSource`() = runBlocking {
        val term = "term"

        coEvery { localDataSource.deleteSearchTermByTerm(term) } just runs

        searchTermsRepository.deleteSearchTermByTerm(term)

        coVerify(exactly = 1) { localDataSource.deleteSearchTermByTerm(term) }
        confirmVerified(localDataSource)
    }

    @Test
    fun `should route getNumberOfSearchTerms call to localDataSource`() = runBlocking {
        val numberOfSearchTerms = 10

        coEvery { localDataSource.getNumberOfSearchTerms() } returns numberOfSearchTerms

        searchTermsRepository.getNumberOfSearchTerms()
            .test()
            .assertNoErrors()
            .assertResult(numberOfSearchTerms)

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
