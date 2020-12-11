package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.models.FactModel
import dev.pimentel.domain.repositories.FactsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFactsTest {

    private val factsRepository = mockk<FactsRepository>()
    private lateinit var useCase: GetFacts

    @BeforeEach
    fun `setup subject`() {
        useCase = GetFacts(factsRepository)
    }

    @Test
    fun `should get search term and then get facts`() = runBlocking {
        val term = "term"
        val factsResponse = listOf(
            object : FactModel {
                override val id: String = "id1"
                override val categories: List<String> = listOf("category1")
                override val url: String = "url1"
                override val value: String = "value1"
            },
            object : FactModel {
                override val id: String = "id2"
                override val categories: List<String> = listOf()
                override val url: String = "url2"
                override val value: String = "value2"
            }
        )
        val expectedResult = listOf(
            Fact("id1", "category1", "url1", "value1"),
            Fact("id2", null, "url2", "value2")
        )

        coEvery { factsRepository.getFacts(term) } returns factsResponse

        assertEquals(useCase(GetFacts.Params(term)), expectedResult)

        coVerify(exactly = 1) { factsRepository.getFacts(term) }
        confirmVerified(factsRepository)
    }
}
