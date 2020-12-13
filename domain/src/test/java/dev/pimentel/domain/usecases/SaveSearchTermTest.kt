package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveSearchTermTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: SaveSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = SaveSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should call searchTermsRepository and just complete`() = runBlocking {
        val term = "term"

        coEvery { searchTermsRepository.saveSearchTerm(term) } just runs

        useCase(SaveSearchTerm.Params(term))

        coVerify(exactly = 1) { searchTermsRepository.saveSearchTerm(term) }
        confirmVerified(searchTermsRepository)
    }
}
