package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AreSearchTermsOnLimitTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: AreSearchTermsOnLimit

    @BeforeEach
    fun `setup subject`() {
        useCase = AreSearchTermsOnLimit(searchTermsRepository)
    }

    @Test
    fun `should return false when search terms are not on limit`() = runBlocking {
        coEvery { searchTermsRepository.getNumberOfSearchTerms() } returns 5

        assertFalse(useCase(NoParams))

        coVerify(exactly = 1) { searchTermsRepository.getNumberOfSearchTerms() }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `should return true when search terms are on limit`() = runBlocking {
        coEvery { searchTermsRepository.getNumberOfSearchTerms() } returns 10

        assertTrue(useCase(NoParams))

        coVerify(exactly = 1) { searchTermsRepository.getNumberOfSearchTerms() }
        confirmVerified(searchTermsRepository)
    }
}
