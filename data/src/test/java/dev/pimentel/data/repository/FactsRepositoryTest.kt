package dev.pimentel.data.repository

import dev.pimentel.data.dto.FactsResponseDTO
import dev.pimentel.data.models.FactModelImpl
import dev.pimentel.data.repositories.FactsRepositoryImpl
import dev.pimentel.data.sources.remote.FactsRemoteDataSource
import dev.pimentel.domain.repositories.FactsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactsRepositoryTest {

    private val remoteDataSource = mockk<FactsRemoteDataSource>()
    private lateinit var factsRepository: FactsRepository

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        factsRepository = FactsRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `should route getFacts call to remoteDataSource`() = runBlocking {
        val term = "term"

        val factResponse = FactsResponseDTO(
            listOf(
                FactsResponseDTO.Fact("id1", listOf("category1"), "url1", "value1"),
                FactsResponseDTO.Fact("id2", listOf("category2"), "url2", "value2")
            )
        )

        val expectedResult = listOf(
            FactModelImpl("id1", listOf("category1"), "url1", "value1"),
            FactModelImpl("id2", listOf("category2"), "url2", "value2")
        )

        coEvery { remoteDataSource.getFacts(term) } returns factResponse

        assertEquals(factsRepository.getFacts(term), expectedResult)

        coVerify(exactly = 1) { remoteDataSource.getFacts(term) }
        confirmVerified(remoteDataSource)
    }
}
