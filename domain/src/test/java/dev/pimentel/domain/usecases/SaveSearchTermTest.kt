package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.SearchTerm
import dev.pimentel.domain.repositories.SearchTermsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SaveSearchTermTest : UseCaseTest<SaveSearchTerm>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: SaveSearchTerm

    override fun `setup subject`() {
        useCase = SaveSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should call searchTermsRepository and just complete`() = runBlocking {
        val term = "term"
        val searchTerm = SearchTerm(term)

        coEvery { searchTermsRepository.saveSearchTerm(searchTerm) } just runs

        useCase(SaveSearchTerm.Params(term))

        coVerify(exactly = 1) { searchTermsRepository.saveSearchTerm(searchTerm) }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `Params must not contain any null properties`() {
        val searchTerm = SearchTerm("term")

        assertNotNull(searchTerm.term)
    }
}
