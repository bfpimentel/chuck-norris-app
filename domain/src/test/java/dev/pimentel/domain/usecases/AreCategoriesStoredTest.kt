package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class AreCategoriesStoredTest : UseCaseTest<AreCategoriesStored>() {

    private val getAllCategories = mockk<GetAllCategories>()
    override lateinit var useCase: AreCategoriesStored

    override fun `setup subject`() {
        useCase = AreCategoriesStored(getAllCategories)
    }

    @Test
    fun `should return false when there are no categories stored`() {
        every { getAllCategories(NoParams) } returns Single.just(listOf())

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(false)

        verify(exactly = 1) { getAllCategories(NoParams) }
        confirmVerified(getAllCategories)
    }

    @Test
    fun `should return true when there categories stored`() {
        every { getAllCategories(NoParams) } returns Single.just(listOf(Category("category1")))

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(true)

        verify(exactly = 1) { getAllCategories(NoParams) }
        confirmVerified(getAllCategories)
    }
}
