package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetAllCategoriesTest : UseCaseTest<GetAllCategories>() {

    private val categoriesRepository = mockk<CategoriesRepository>()
    override lateinit var useCase: GetAllCategories

    override fun `setup subject`() {
        useCase = GetAllCategories(categoriesRepository)
    }

    @Test
    fun `should return a list of categories`() {
        val categories = listOf(
            CategoryModel("name1"),
            CategoryModel("name2")
        )

        every { categoriesRepository.getAllCategories() } returns Single.just(categories)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categories)

        verify(exactly = 1) { categoriesRepository.getAllCategories() }
        confirmVerified(categoriesRepository)
    }
}
