package dev.pimentel.chucknorris.presentation

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.FactsContract
import dev.pimentel.chucknorris.presentation.facts.FactsViewModel
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`() {
        viewModel = FactsViewModel(
            navigator,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    fun `should navigate to search`() {
        every { navigator.navigate(R.id.search_fragment) } just runs

        viewModel.navigateToSearch()

        verify(exactly = 1) { navigator.navigate(R.id.search_fragment) }
        confirmVerified(navigator)
    }
}
