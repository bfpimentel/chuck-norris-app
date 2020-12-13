package dev.pimentel.domain.usecases

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

class SaveNonExistingSearchTermTest {

    private val areSearchTermsOnLimit = mockk<AreSearchTermsOnLimit>()
    private val deleteLastSearchTerm = mockk<DeleteLastSearchTerm>()
    private val saveSearchTerm = mockk<SaveSearchTerm>()
    private lateinit var useCase: SaveNonExistingSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = SaveNonExistingSearchTerm(
            areSearchTermsOnLimit,
            deleteLastSearchTerm,
            saveSearchTerm
        )
    }

    @Test
    fun `should delete last search term and save the new one when search terms are on limit`() =
        runBlocking {
            val term = "term"
            val saveSearchTermParams = SaveSearchTerm.Params(term)

            coEvery { areSearchTermsOnLimit(NoParams) } returns true
            coEvery { deleteLastSearchTerm(NoParams) } just runs
            coEvery { saveSearchTerm(saveSearchTermParams) } just runs

            useCase(SaveNonExistingSearchTerm.Params(term))

            coVerify(exactly = 1) {
                areSearchTermsOnLimit(NoParams)
                deleteLastSearchTerm(NoParams)
                saveSearchTerm(saveSearchTermParams)
            }
            confirmVerified(areSearchTermsOnLimit, deleteLastSearchTerm, saveSearchTerm)
        }

    @Test
    fun `should just save new search term when search terms are not on limit`() = runBlocking {
        val term = "term"
        val saveSearchTermParams = SaveSearchTerm.Params(term)

        coEvery { areSearchTermsOnLimit(NoParams) } returns false
        coEvery { saveSearchTerm(saveSearchTermParams) } just runs

        useCase(SaveNonExistingSearchTerm.Params(term))

        coVerify(exactly = 1) {
            areSearchTermsOnLimit(NoParams)
            saveSearchTerm(saveSearchTermParams)
        }
        confirmVerified(areSearchTermsOnLimit, deleteLastSearchTerm, saveSearchTerm)
    }
}
