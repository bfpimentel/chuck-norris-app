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

class GetSearchTermTest : UseCaseTest<GetSearchTerm>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: GetSearchTerm

    override fun `setup subject`() {
        useCase = GetSearchTerm(searchTermsRepository)
    }

    @Test
    fun `should get search term and map it to string`() {
        val term = "term"
        val searchTerm = SearchTerm(0, term)

        every { searchTermsRepository.getSearchTerm() } returns Single.just(searchTerm)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(term)

        verify(exactly = 1) { searchTermsRepository.getSearchTerm() }
        confirmVerified(searchTermsRepository)
    }
}
