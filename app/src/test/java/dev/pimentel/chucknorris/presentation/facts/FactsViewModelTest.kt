package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.ViewModelTest
import io.mockk.*
import org.junit.jupiter.api.Test

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`() {
        viewModel = FactsViewModel(
            navigator,
            getErrorType,
            schedulerProvider
        )
    }

    @Test
    fun `should navigate to search`() {
        every { navigator.navigate(R.id.search_fragment) } just runs

        viewModel.navigateToSearch()

        verify(exactly = 1) { navigator.navigate(R.id.search_fragment) }
        confirmVerified(navigator, getErrorType)
    }
}
