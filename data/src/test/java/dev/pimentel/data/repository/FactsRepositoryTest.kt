package dev.pimentel.data.repository

import dev.pimentel.data.models.FactsResponse
import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.data.repositories.FactsRepositoryImpl
import dev.pimentel.data.sources.FactsRemoteDataSource
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
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
    fun `should route getFacts call to remoteDataSource`() {
        val term = "term"
        val factResponse = FactsResponse(
            listOf(
                FactsResponse.Fact(listOf("category1"), "url1", "value1"),
                FactsResponse.Fact(listOf("category2"), "url2", "value2")
            )
        )

        every { remoteDataSource.getFacts(term) } returns Single.just(factResponse)

        factsRepository.getFacts(term)
            .test()
            .assertNoErrors()
            .assertResult(factResponse)

        verify(exactly = 1) { remoteDataSource.getFacts(term) }
        confirmVerified(remoteDataSource)
    }
}
