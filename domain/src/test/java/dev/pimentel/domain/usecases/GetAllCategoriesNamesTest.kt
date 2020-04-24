package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetAllCategoriesNamesTest : UseCaseTest<GetAllCategoriesNames>() {

    private val categoriesRepository = mockk<CategoriesRepository>()
    override lateinit var useCase: GetAllCategoriesNames


    override fun `setup subject`() {
        useCase = GetAllCategoriesNames(categoriesRepository)
    }

    @Test
    fun `should return a list of categories names`() {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        every { categoriesRepository.getAllCategoriesNames() } returns Single.just(categoriesNames)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categoriesNames)

        verify(exactly = 1) { categoriesRepository.getAllCategoriesNames() }
        confirmVerified(categoriesRepository)
    }
}
