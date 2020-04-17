package dev.pimentel.domain.usecases

import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchAllCategoriesNamesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var fetchAllCategoriesNames: FetchAllCategoriesNames

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        fetchAllCategoriesNames = FetchAllCategoriesNames(categoriesRepository)

        assertNotNull(fetchAllCategoriesNames)
    }

    @Test
    fun `should route fetchAllCategoriesNames call to categoriesRepository`() {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        every {
            categoriesRepository.fetchAllCategoriesNames()
        } returns Single.just(categoriesNames)

        fetchAllCategoriesNames(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categoriesNames)

        verify(exactly = 1) { categoriesRepository.fetchAllCategoriesNames() }
        confirmVerified(categoriesRepository)
    }
}
