package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
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

class FetchAllCategoriesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var fetchAllCategories: FetchAllCategories

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        fetchAllCategories = FetchAllCategories(categoriesRepository)

        assertNotNull(categoriesRepository)
    }

    @Test
    fun `should route fetchAllCategories call to categoriesRepository`() {
        val categories = listOf(
            Category("name1"),
            Category("name2")
        )

        every { categoriesRepository.fetchAllCategories() } returns Single.just(categories)

        fetchAllCategories(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categories)

        verify(exactly = 1) { categoriesRepository.fetchAllCategories() }
        confirmVerified(categoriesRepository)
    }
}
