package dev.pimentel.domain.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HandleSearchTermSavingTest {

    private val doesSearchTermExist = mockk<DoesSearchTermExist>()
    private val saveExistingSearchTerm = mockk<SaveExistingSearchTerm>()
    private val saveNonExistingSearchTerm = mockk<SaveNonExistingSearchTerm>()
    private lateinit var useCase: HandleSearchTermSaving

    @BeforeEach
    fun `setup subject`() {
        useCase = HandleSearchTermSaving(
            doesSearchTermExist,
            saveExistingSearchTerm,
            saveNonExistingSearchTerm
        )
    }

    @Test
    fun `should save existing search term when search term already exists`() = runBlocking {
        val term = "term"
        val doesSearchTermExistParams = DoesSearchTermExist.Params(term)
        val saveExistingSearchTermParams = SaveExistingSearchTerm.Params(term)

        coEvery { doesSearchTermExist(doesSearchTermExistParams) } returns true
        coEvery { saveExistingSearchTerm(saveExistingSearchTermParams) } just runs

        useCase(HandleSearchTermSaving.Params(term))

        coVerify(exactly = 1) {
            doesSearchTermExist(doesSearchTermExistParams)
            saveExistingSearchTerm(saveExistingSearchTermParams)
        }
        confirmVerified(doesSearchTermExist, saveExistingSearchTerm, saveNonExistingSearchTerm)
    }

    @Test
    fun `should save non existing search term when search term does not exist`() = runBlocking {
        val term = "term"
        val doesSearchTermExistParams = DoesSearchTermExist.Params(term)
        val saveNonExistingSearchTermParams = SaveNonExistingSearchTerm.Params(term)

        coEvery { doesSearchTermExist(doesSearchTermExistParams) } returns false
        coEvery { saveNonExistingSearchTerm(saveNonExistingSearchTermParams) } just runs

        useCase(HandleSearchTermSaving.Params(term))

        coVerify(exactly = 1) {
            doesSearchTermExist(doesSearchTermExistParams)
            saveNonExistingSearchTerm(saveNonExistingSearchTermParams)
        }
        confirmVerified(doesSearchTermExist, saveExistingSearchTerm, saveNonExistingSearchTerm)
    }
}
