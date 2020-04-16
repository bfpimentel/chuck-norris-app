package dev.pimentel.chucknorris.shared.abstractions

import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.chucknorris.shared.abstractions.BaseContract
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import org.junit.jupiter.api.Assertions.assertNotNull

interface NullableFieldsContract {

    interface ViewModel : BaseContract.ViewModel
}

class NullableFieldsViewModel : BaseViewModel(), NullableFieldsContract.ViewModel

class NullableFieldsViewModelTest : ViewModelTest<NullableFieldsContract.ViewModel>() {

    override lateinit var viewModel: NullableFieldsContract.ViewModel

    override fun setupSubject() {
        viewModel = NullableFieldsViewModel()

        assertNotNull(viewModel)
    }
}
