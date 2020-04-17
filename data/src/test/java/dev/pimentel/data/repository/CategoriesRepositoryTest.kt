package dev.pimentel.data.repository

import dev.pimentel.data.models.Category
import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.data.repositories.CategoriesRepositoryImpl
import dev.pimentel.data.sources.CategoriesLocalDataSource
import dev.pimentel.data.sources.CategoriesRemoteDataSource
import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoriesRepositoryTest {

    private val remoteDataSource = mockk<CategoriesRemoteDataSource>()
    private val localDataSource = mockk<CategoriesLocalDataSource>()
    private lateinit var categoriesRepository: CategoriesRepository

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        categoriesRepository = CategoriesRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        assertNotNull(categoriesRepository)
    }

    @Test
    fun `should return a list of categories when fetching all categories`() {
        val categories = listOf(
            Category("name1"),
            Category("name2")
        )

        every { localDataSource.fetchAllCategories() } returns Single.just(categories)

        categoriesRepository.fetchAllCategories()
            .test()
            .assertNoErrors()
            .assertResult(categories)

        verify(exactly = 1) { localDataSource.fetchAllCategories() }
        confirmVerified(localDataSource, remoteDataSource)
    }

    @Test
    fun `should return a list of categories names when fetching all categories names`() {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        every { remoteDataSource.fetchAllCategoriesNames() } returns Single.just(categoriesNames)

        categoriesRepository.fetchAllCategoriesNames()
            .test()
            .assertNoErrors()
            .assertResult(categoriesNames)

        verify(exactly = 1) { remoteDataSource.fetchAllCategoriesNames() }
        confirmVerified(localDataSource, remoteDataSource)
    }

    @Test
    fun `should just route saveAllCategories call to localDataSource`() {
        val categories = listOf(
            Category("name1"),
            Category("name2")
        )

        every { localDataSource.saveAllCategories(categories) } just runs

        categoriesRepository.saveAllCategories(categories)

        verify(exactly = 1) { localDataSource.saveAllCategories(categories) }
        confirmVerified(localDataSource, remoteDataSource)
    }
}
