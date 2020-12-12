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

class SaveExistingSearchTermTest {

    private val deleteSearchTerm = mockk<DeleteSearchTerm>()
    private val saveSearchTerm = mockk<SaveSearchTerm>()
    private lateinit var useCase: SaveExistingSearchTerm

    @BeforeEach
    fun `setup subject`() {
        useCase = SaveExistingSearchTerm(
            deleteSearchTerm,
            saveSearchTerm
        )
    }

    @Test
    fun `should delete search term and save it again when saving existing search term`() =
        runBlocking {
            val term = "term"
            val deleteSearchTermParams = DeleteSearchTerm.Params(term)
            val saveSearchTermParams = SaveSearchTerm.Params(term)

            coEvery { deleteSearchTerm(deleteSearchTermParams) } just runs
            coEvery { saveSearchTerm(saveSearchTermParams) } just runs

            useCase(SaveExistingSearchTerm.Params(term))

            coVerify(exactly = 1) {
                deleteSearchTerm(deleteSearchTermParams)
                saveSearchTerm(saveSearchTermParams)
            }
            confirmVerified(deleteSearchTerm, saveSearchTerm)
        }
}
