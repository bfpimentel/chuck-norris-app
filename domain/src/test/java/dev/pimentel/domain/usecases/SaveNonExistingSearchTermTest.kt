package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Test

class SaveNonExistingSearchTermTest : UseCaseTest<SaveNonExistingSearchTerm>() {

    private val areSearchTermsOnLimit = mockk<AreSearchTermsOnLimit>()
    private val deleteLastSearchTerm = mockk<DeleteLastSearchTerm>()
    private val saveSearchTerm = mockk<SaveSearchTerm>()
    override lateinit var useCase: SaveNonExistingSearchTerm

    override fun `setup subject`() {
        useCase = SaveNonExistingSearchTerm(
            areSearchTermsOnLimit,
            deleteLastSearchTerm,
            saveSearchTerm
        )
    }

    @Test
    fun `should delete last search term and save the new one when search terms are on limit`() {
        val term = "term"
        val saveSearchTermParams = SaveSearchTerm.Params(term)

        every { areSearchTermsOnLimit(NoParams) } returns Single.just(true)
        every { deleteLastSearchTerm(NoParams) } returns Completable.complete()
        every { saveSearchTerm(saveSearchTermParams) } returns Completable.complete()

        useCase(SaveNonExistingSearchTerm.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) {
            areSearchTermsOnLimit(NoParams)
            deleteLastSearchTerm(NoParams)
            saveSearchTerm(saveSearchTermParams)
        }
        confirmVerified(areSearchTermsOnLimit, deleteLastSearchTerm, saveSearchTerm)
    }

    @Test
    fun `should just save new search term when search terms are not on limit`() {
        val term = "term"
        val saveSearchTermParams = SaveSearchTerm.Params(term)

        every { areSearchTermsOnLimit(NoParams) } returns Single.just(false)
        every { saveSearchTerm(saveSearchTermParams) } returns Completable.complete()

        useCase(SaveNonExistingSearchTerm.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) {
            areSearchTermsOnLimit(NoParams)
            saveSearchTerm(saveSearchTermParams)
        }
        confirmVerified(areSearchTermsOnLimit, deleteLastSearchTerm, saveSearchTerm)
    }
}
