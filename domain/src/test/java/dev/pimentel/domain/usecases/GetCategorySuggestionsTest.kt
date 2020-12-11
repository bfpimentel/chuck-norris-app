package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetCategorySuggestionsTest : UseCaseTest<GetCategorySuggestions>() {

    private val fetchAllCategories = mockk<GetAllCategories>()
    private val fetchAllCategoriesNames = mockk<GetAllCategoriesNames>()
    private val saveAllCategories = mockk<SaveAllCategories>()
    private val shuffleList = mockk<ShuffleList>()
    override lateinit var useCase: GetCategorySuggestions

    override fun `setup subject`() {
        useCase = GetCategorySuggestions(
            fetchAllCategories,
            shuffleList
        )
    }

    @Test
    fun `should return a categories list with 8 random items when there are already saved items`() {
        val shuffleListParams = ShuffleList.Params(allCategories)

        every { fetchAllCategories(NoParams) } returns Single.just(allCategories)
        every { shuffleList(shuffleListParams) } returns allCategories

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categorySuggestions)

        verify(exactly = 1) {
            fetchAllCategories(NoParams)
            shuffleList(shuffleListParams)
        }
        confirmVerified(fetchAllCategories, fetchAllCategoriesNames, saveAllCategories, shuffleList)
    }

    private companion object {
        val allCategories = listOf(
            CategoryModel("name0"),
            CategoryModel("name1"),
            CategoryModel("name2"),
            CategoryModel("name3"),
            CategoryModel("name4"),
            CategoryModel("name5"),
            CategoryModel("name6"),
            CategoryModel("name7"),
            CategoryModel("name8"),
            CategoryModel("name9")
        )

        val categorySuggestions = listOf(
            "name0",
            "name1",
            "name2",
            "name3",
            "name4",
            "name5",
            "name6",
            "name7"
        )
    }
}
