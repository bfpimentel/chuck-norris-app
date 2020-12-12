package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DoesSearchTermExistTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: DoesSearchTermExist

    @BeforeEach
    fun `setup subject`() {
        useCase = DoesSearchTermExist(searchTermsRepository)
    }

    @Test
    fun `should return false when search term does not exist`() = runBlocking {
        val term = "term"
        val searchTerms = emptyList<String>()

        coEvery { searchTermsRepository.getSearchTermByTerm(term) } returns searchTerms

        assertFalse(useCase(DoesSearchTermExist.Params(term)))

        coVerify(exactly = 1) { searchTermsRepository.getSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `should return true when search term exists`() = runBlocking {
        val term = "term"
        val searchTerms = listOf(term)

        coEvery { searchTermsRepository.getSearchTermByTerm(term) } returns searchTerms

        assertTrue(useCase(DoesSearchTermExist.Params(term)))

        coVerify(exactly = 1) { searchTermsRepository.getSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }
}
