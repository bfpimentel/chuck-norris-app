package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLastSearchTermsTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: GetLastSearchTerms

    @BeforeEach
    fun `setup subject`() {
        useCase = GetLastSearchTerms(searchTermsRepository)
    }

    @Test
    fun `should fetch last search terms and map them to string`() = runBlocking {
        val searchTerms = listOf(
            "term1",
            "term2"
        )

        val expectedResult = listOf(
            "term1",
            "term2"
        )

        coEvery { searchTermsRepository.getLastSearchTerms() } returns searchTerms

        assertEquals(useCase(NoParams), expectedResult)

        coVerify(exactly = 1) { searchTermsRepository.getLastSearchTerms() }
        confirmVerified(searchTermsRepository)
    }
}
