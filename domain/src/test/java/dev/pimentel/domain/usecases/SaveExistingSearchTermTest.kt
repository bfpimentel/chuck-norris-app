package dev.pimentel.domain.usecases

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.jupiter.api.Test

class SaveExistingSearchTermTest : UseCaseTest<SaveExistingSearchTerm>() {

    private val deleteSearchTerm = mockk<DeleteSearchTerm>()
    private val saveSearchTerm = mockk<SaveSearchTerm>()
    override lateinit var useCase: SaveExistingSearchTerm

    override fun `setup subject`() {
        useCase = SaveExistingSearchTerm(
            deleteSearchTerm,
            saveSearchTerm
        )
    }

    @Test
    fun `should delete search term and save it again when saving existing search term`() {
        val term = "term"
        val deleteSearchTermParams = DeleteSearchTerm.Params(term)
        val saveSearchTermParams = SaveSearchTerm.Params(term)

        every { deleteSearchTerm(deleteSearchTermParams) } returns Completable.complete()
        every { saveSearchTerm(saveSearchTermParams) } returns Completable.complete()

        useCase(SaveExistingSearchTerm.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) {
            deleteSearchTerm(deleteSearchTermParams)
            saveSearchTerm(saveSearchTermParams)
        }
        confirmVerified(deleteSearchTerm, saveSearchTerm)
    }
}
