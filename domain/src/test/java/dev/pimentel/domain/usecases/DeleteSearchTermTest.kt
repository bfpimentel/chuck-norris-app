package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.SearchTermsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class DeleteSearchTermTest : UseCaseTest<DeleteSearchTerm>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: DeleteSearchTerm

    override fun `setup subject`() {
        useCase = DeleteSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should delete search term and then just complete`() {
        val term = "term"

        every { searchTermsRepository.deleteSearchTermByTerm(term) } just runs

        useCase(DeleteSearchTerm.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { searchTermsRepository.deleteSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }
}
