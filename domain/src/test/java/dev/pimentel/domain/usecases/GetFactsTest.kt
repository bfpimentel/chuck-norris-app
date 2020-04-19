package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test
import dev.pimentel.data.models.Fact as FactModel
import dev.pimentel.domain.entities.Fact as FactEntity

class GetFactsTest : UseCaseTest<GetFacts>() {

    private val getSearchTerm = mockk<GetSearchTerm>()
    private val factsRepository = mockk<FactsRepository>()
    override lateinit var useCase: GetFacts

    override fun `setup subject`() {
        useCase = GetFacts(
            getSearchTerm,
            factsRepository
        )
    }

    @Test
    fun `should get search term and then get facts`() {
        val term = "term"
        val factsModels = listOf(
            FactModel(listOf("category1"), "url1", "value1"),
            FactModel(listOf("category2"), "url2", "value2")
        )
        val factsEntities = listOf(
            FactEntity(listOf("category1"), "url1", "value1"),
            FactEntity(listOf("category2"), "url2", "value2")
        )

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { factsRepository.getFacts(term) } returns Single.just(factsModels)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(factsEntities)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            factsRepository.getFacts(term)
        }
        confirmVerified(getSearchTerm, factsRepository)
    }
}
