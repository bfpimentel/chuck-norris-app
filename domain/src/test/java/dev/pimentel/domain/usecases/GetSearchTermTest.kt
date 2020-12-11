package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetSearchTermTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: GetSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = GetSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should get search term and map it to string after getting it successfully from repository`() =
        runBlocking {
            val term = "term"
            val expectedResult = "term"

            coEvery { searchTermsRepository.getSearchTerm() } returns term

            Assertions.assertEquals(useCase(NoParams), expectedResult)

            coVerify(exactly = 1) { searchTermsRepository.getSearchTerm() }
            confirmVerified(searchTermsRepository)
        }

    @Test
    fun `should return SearchTermNotFoundException when failing to get search term from repository`() =
        runBlocking {
            coEvery {
                searchTermsRepository.getSearchTerm()
            } throws IllegalStateException()

            assertThrows<GetSearchTerm.SearchTermNotFoundException> { useCase(NoParams) }

            coVerify(exactly = 1) { searchTermsRepository.getSearchTerm() }
            confirmVerified(searchTermsRepository)
        }
}
