package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteLastSearchTermTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: DeleteLastSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = DeleteLastSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should delete last search term and then just complete`() = runBlocking {
        coEvery { searchTermsRepository.deleteLastSearchTerm() } just runs

        useCase(NoParams)

        coVerify(exactly = 1) { searchTermsRepository.deleteLastSearchTerm() }
        confirmVerified(searchTermsRepository)
    }
}
