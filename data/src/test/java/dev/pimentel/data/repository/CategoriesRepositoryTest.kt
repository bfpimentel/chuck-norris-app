package dev.pimentel.data.repository

import dev.pimentel.data.dto.CategoryDTO
import dev.pimentel.data.repositories.CategoriesRepositoryImpl
import dev.pimentel.data.sources.local.CategoriesLocalDataSource
import dev.pimentel.data.sources.remote.CategoriesRemoteDataSource
import dev.pimentel.domain.repositories.CategoriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoriesRepositoryTest {

    private val remoteDataSource = mockk<CategoriesRemoteDataSource>()
    private val localDataSource = mockk<CategoriesLocalDataSource>()
    private lateinit var categoriesRepository: CategoriesRepository

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() = runBlocking {
        categoriesRepository = CategoriesRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        assertNotNull(categoriesRepository)
    }

    @Test
    fun `should route getAllCategories call to localDataSource`() = runBlocking {
        val categories = listOf(
            CategoryDTO("name1"),
            CategoryDTO("name2")
        )

        val expectedResult = listOf(
            "name1",
            "name2",
        )

        coEvery { localDataSource.getAllCategories() } returns categories

        assertEquals(categoriesRepository.getAllCategories(), expectedResult)

        coVerify(exactly = 1) { localDataSource.getAllCategories() }
        confirmVerified(localDataSource, remoteDataSource)
    }

    @Test
    fun `should route getAllCategoriesNames call to remoteDataSource`() = runBlocking {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        coEvery { remoteDataSource.getAllCategoriesNames() } returns categoriesNames

        assertEquals(categoriesRepository.getAllCategoriesNames(), categoriesNames)

        coVerify(exactly = 1) { remoteDataSource.getAllCategoriesNames() }
        confirmVerified(localDataSource, remoteDataSource)
    }

    @Test
    fun `should just route saveAllCategories call to localDataSource`() = runBlocking {
        val categories = listOf(
            CategoryDTO("name1"),
            CategoryDTO("name2")
        )

        val expectedResult = listOf(
            "name1",
            "name2",
        )

        coEvery { localDataSource.saveAllCategories(categories) } just runs

        categoriesRepository.saveAllCategories(expectedResult)

        coVerify(exactly = 1) { localDataSource.saveAllCategories(categories) }
        confirmVerified(localDataSource, remoteDataSource)
    }
}
