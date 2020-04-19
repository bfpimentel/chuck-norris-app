package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SaveSearchTermTest : UseCaseTest<SaveSearchTerm>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: SaveSearchTerm

    override fun `setup subject`() {
        useCase = SaveSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should call searchTermsRepository and just complete`() {
        val term = "term"
        val searchTerm = SearchTerm(0, term)

        every { searchTermsRepository.saveSearchTerm(searchTerm) } just runs

        useCase(SaveSearchTerm.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { searchTermsRepository.saveSearchTerm(searchTerm) }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `Params must not contain any null properties`() {
        val searchTerm = SearchTerm(0, "term")

        assertNotNull(searchTerm.id)
        assertNotNull(searchTerm.term)
    }
}
