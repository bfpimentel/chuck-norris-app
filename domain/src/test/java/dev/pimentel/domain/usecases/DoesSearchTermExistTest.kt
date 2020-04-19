package dev.pimentel.domain.usecases

import dev.pimentel.data.models.SearchTerm
import dev.pimentel.data.repositories.SearchTermsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class DoesSearchTermExistTest : UseCaseTest<DoesSearchTermExist>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: DoesSearchTermExist

    override fun `setup subject`() {
        useCase = DoesSearchTermExist(searchTermsRepository)
    }

    @Test
    fun `should return false when search term does not exist`() {
        val term = "term"
        val searchTerms = listOf<SearchTerm>()

        every { searchTermsRepository.getSearchTermByTerm(term) } returns Single.just(searchTerms)

        useCase(DoesSearchTermExist.Params(term))
            .test()
            .assertNoErrors()
            .assertResult(false)

        verify(exactly = 1) { searchTermsRepository.getSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `should return true when search term exists`() {
        val term = "term"
        val searchTerms = listOf(SearchTerm(0, term))

        every { searchTermsRepository.getSearchTermByTerm(term) } returns Single.just(searchTerms)

        useCase(DoesSearchTermExist.Params(term))
            .test()
            .assertNoErrors()
            .assertResult(true)

        verify(exactly = 1) { searchTermsRepository.getSearchTermByTerm(term) }
        confirmVerified(searchTermsRepository)
    }
}