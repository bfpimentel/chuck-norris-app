package dev.pimentel.domain.usecases

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Test

class HandleSearchTermSavingTest : UseCaseTest<HandleSearchTermSaving>() {

    private val doesSearchTermExist = mockk<DoesSearchTermExist>()
    private val saveExistingSearchTerm = mockk<SaveExistingSearchTerm>()
    private val saveNonExistingSearchTerm = mockk<SaveNonExistingSearchTerm>()
    override lateinit var useCase: HandleSearchTermSaving

    override fun `setup subject`() {
        useCase = HandleSearchTermSaving(
            doesSearchTermExist,
            saveExistingSearchTerm,
            saveNonExistingSearchTerm
        )
    }

    @Test
    fun `should save existing search term when search term already exists`() {
        val term = "term"
        val doesSearchTermExistParams = DoesSearchTermExist.Params(term)
        val saveExistingSearchTermParams = SaveExistingSearchTerm.Params(term)

        every { doesSearchTermExist(doesSearchTermExistParams) } returns Single.just(true)
        every { saveExistingSearchTerm(saveExistingSearchTermParams) } returns Completable.complete()

        useCase(HandleSearchTermSaving.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) {
            doesSearchTermExist(doesSearchTermExistParams)
            saveExistingSearchTerm(saveExistingSearchTermParams)
        }
        confirmVerified(doesSearchTermExist, saveExistingSearchTerm, saveNonExistingSearchTerm)
    }

    @Test
    fun `should save non existing search term when search term does not exist`() {
        val term = "term"
        val doesSearchTermExistParams = DoesSearchTermExist.Params(term)
        val saveNonExistingSearchTermParams = SaveNonExistingSearchTerm.Params(term)

        every { doesSearchTermExist(doesSearchTermExistParams) } returns Single.just(false)
        every { saveNonExistingSearchTerm(saveNonExistingSearchTermParams) } returns Completable.complete()

        useCase(HandleSearchTermSaving.Params(term))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) {
            doesSearchTermExist(doesSearchTermExistParams)
            saveNonExistingSearchTerm(saveNonExistingSearchTermParams)
        }
        confirmVerified(doesSearchTermExist, saveExistingSearchTerm, saveNonExistingSearchTerm)
    }
}
