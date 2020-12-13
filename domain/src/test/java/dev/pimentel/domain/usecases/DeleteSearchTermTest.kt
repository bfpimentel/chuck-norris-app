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

class DeleteSearchTermTest {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    private lateinit var useCase: DeleteSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = DeleteSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should delete search term and then just complete`() = runBlocking {
        val term = "term"

        coEvery { searchTermsRepository.deleteSearchTermByTerm(term) } just runs

        useCase(DeleteSearchTerm.Params(term))

        coVerify(exactly = 1) { searchTermsRepository.deleteSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }
}
