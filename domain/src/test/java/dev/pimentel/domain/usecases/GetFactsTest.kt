package dev.pimentel.domain.usecases

import android.content.Context
import dev.pimentel.data.models.FactsResponse
import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.R
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test
import dev.pimentel.data.models.FactsResponse.Fact as FactModel
import dev.pimentel.domain.entities.Fact as FactEntity

class GetFactsTest : UseCaseTest<GetFacts>() {

    private val factsRepository = mockk<FactsRepository>()
    private val context = mockk<Context>(relaxed = true)
    override lateinit var useCase: GetFacts

    override fun `setup subject`() {
        useCase = GetFacts(
            factsRepository,
            context
        )
    }

    @Test
    fun `should get search term and then get facts`() {
        val term = "term"
        val factsResponse = FactsResponse(
            listOf(
                FactModel(listOf("category1"), "url1", "value1"),
                FactModel(listOf(), "url2", "value2")
            )
        )
        val uncategorized = "uncategorized"
        val facts = listOf(
            FactEntity("category1", "url1", "value1"),
            FactEntity(uncategorized, "url2", "value2")
        )

        every { factsRepository.getFacts(term) } returns Single.just(factsResponse)
        every { context.getString(R.string.get_facts_no_category) } returns uncategorized

        useCase(GetFacts.Params(term))
            .test()
            .assertNoErrors()
            .assertResult(facts)

        verify(exactly = 1) {
            factsRepository.getFacts(term)
            context.getString(R.string.get_facts_no_category)
        }
        confirmVerified(factsRepository, context)
    }
}
