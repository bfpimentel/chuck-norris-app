package dev.pimentel.chucknorris.shared.abstractions

import dev.pimentel.chucknorris.testshared.ViewModelTest
import org.junit.jupiter.api.Assertions.assertNotNull

interface NullableFieldsContract {

    interface ViewModel : BaseContract.ViewModel
}

class NullableFieldsViewModel : RxViewModel(), NullableFieldsContract.ViewModel

class NullableFieldsViewModelTest : ViewModelTest<NullableFieldsContract.ViewModel>() {

    override lateinit var viewModel: NullableFieldsContract.ViewModel

    override fun `setup subject`() {
        viewModel = NullableFieldsViewModel()

        assertNotNull(viewModel)
    }
}
