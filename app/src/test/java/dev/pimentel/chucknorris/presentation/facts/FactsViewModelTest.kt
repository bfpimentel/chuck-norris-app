package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.testshared.ViewModelTest
import org.junit.jupiter.api.Assertions.assertNotNull

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`() {
        viewModel = FactsViewModel(getErrorType, schedulerProvider)

        assertNotNull(viewModel)
    }
}
