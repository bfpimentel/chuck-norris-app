package dev.pimentel.domain.usecases

import android.content.Context
import dev.pimentel.domain.R
import dev.pimentel.domain.models.FactsResponse
import dev.pimentel.domain.repositories.FactsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test
import dev.pimentel.domain.entities.Fact as FactEntity
import dev.pimentel.domain.models.FactsResponse.Fact as FactModel

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
                FactModel("id1", listOf("category1"), "url1", "value1"),
                FactModel("id2", listOf(), "url2", "value2")
            )
        )
        val uncategorized = "uncategorized"
        val facts = listOf(
            FactEntity("id1", "category1", "url1", "value1"),
            FactEntity("id2", uncategorized, "url2", "value2")
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
