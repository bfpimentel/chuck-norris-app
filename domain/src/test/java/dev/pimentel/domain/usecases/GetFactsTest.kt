package dev.pimentel.domain.usecases

import android.content.Context
import dev.pimentel.data.models.FactsResponse
import dev.pimentel.data.repositories.FactsRepository
import dev.pimentel.domain.R
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test
import dev.pimentel.data.models.FactsResponse.Fact as FactModel
import dev.pimentel.domain.entities.Fact as FactEntity

class GetFactsTest : UseCaseTest<GetFacts>() {

    private val getSearchTerm = mockk<GetSearchTerm>()
    private val factsRepository = mockk<FactsRepository>()
    private val context = mockk<Context>(relaxed = true)
    override lateinit var useCase: GetFacts

    override fun `setup subject`() {
        useCase = GetFacts(
            getSearchTerm,
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

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { factsRepository.getFacts(term) } returns Single.just(factsResponse)
        every { context.getString(R.string.get_facts_no_category) } returns uncategorized

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(facts)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            factsRepository.getFacts(term)
            context.getString(R.string.get_facts_no_category)
        }
        confirmVerified(getSearchTerm, factsRepository)
    }
}
