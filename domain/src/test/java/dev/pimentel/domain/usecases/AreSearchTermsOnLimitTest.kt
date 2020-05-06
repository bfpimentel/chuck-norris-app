package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.SearchTermsRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class AreSearchTermsOnLimitTest : UseCaseTest<AreSearchTermsOnLimit>() {

    private val searchTermsRepository = mockk<SearchTermsRepository>()
    override lateinit var useCase: AreSearchTermsOnLimit

    override fun `setup subject`() {
        useCase = AreSearchTermsOnLimit(searchTermsRepository)
    }

    @Test
    fun `should return false when search terms are not on limit`() {
        every { searchTermsRepository.getNumberOfSearchTerms() } returns Single.just(5)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(false)

        verify(exactly = 1) { searchTermsRepository.getNumberOfSearchTerms() }
        confirmVerified(searchTermsRepository)
    }

    @Test
    fun `should return true when search terms are on limit`() {
        every { searchTermsRepository.getNumberOfSearchTerms() } returns Single.just(10)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(true)

        verify(exactly = 1) { searchTermsRepository.getNumberOfSearchTerms() }
        confirmVerified(searchTermsRepository)
    }
}
