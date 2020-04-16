package dev.pimentel.chucknorris.facts

import dev.pimentel.chucknorris.shared.ViewModelTest
import org.junit.jupiter.api.Assertions.assertNotNull

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    override lateinit var viewModel: FactsContract.ViewModel

    override fun setupSubject() {
        viewModel = FactsViewModel(getErrorType, schedulerProvider)

        assertNotNull(viewModel)
    }
}
