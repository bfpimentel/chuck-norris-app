package dev.pimentel.chucknorris.testshared

import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.dispatchersprovider.DispatchersProvider
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class ViewModelTest<ViewModelType> {

    protected lateinit var getErrorMessage: GetErrorMessage
    protected lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var dispatchersProvider: DispatchersProvider

    abstract val viewModel: ViewModelType

    abstract fun `setup subject`(dispatchersProvider: DispatchersProvider)

    @BeforeEach
    fun `should setup subject and then the test dependencies must not be null`() {
        testDispatcher = TestCoroutineDispatcher()
        dispatchersProvider = TestDispatchersProvider(testDispatcher)
        getErrorMessage = mockk()

        Dispatchers.setMain(testDispatcher)

        `setup subject`(dispatchersProvider)
    }

    @AfterEach
    fun `tear down`() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
