package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class DeleteLastSearchTermTest : UseCaseTest<DeleteLastSearchTerm>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: DeleteLastSearchTerm

    override fun `setup subject`() {
        useCase = DeleteLastSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should delete last search term and then just complete`() {
        every { searchTermsRepository.deleteLastSearchTerm() } just runs

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { searchTermsRepository.deleteLastSearchTerm() }
        confirmVerified(searchTermsRepository)
    }
}
