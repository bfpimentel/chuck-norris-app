package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetLastSearchTermsTest : UseCaseTest<GetLastSearchTerms>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: GetLastSearchTerms

    override fun `setup subject`() {
        useCase = GetLastSearchTerms(searchTermsRepository)
    }

    @Test
    fun `should fetch last search terms and map them to string`() {
        val searchTerms = listOf(
            SearchTerm(0, "term1"),
            SearchTerm(1, "term2")
        )
        val terms = listOf(
            "term1",
            "term2"
        )

        every { searchTermsRepository.getLastSearchTerms() } returns Single.just(searchTerms)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(terms)

        verify(exactly = 1) { searchTermsRepository.getLastSearchTerms() }
        confirmVerified(searchTermsRepository)
    }
}
