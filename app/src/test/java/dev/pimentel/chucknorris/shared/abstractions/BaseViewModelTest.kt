package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.chucknorris.shared.abstractions.BaseContract
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.ErrorType
import dev.pimentel.domain.usecases.GetErrorTypeParams
import dev.pimentel.domain.usecases.GetErrorType
import dev.pimentel.domain.usecases.NoParams
import dev.pimentel.domain.usecases.UseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestCompletable : UseCase<NoParams, Completable> {

    override fun invoke(params: NoParams): Completable = Completable.complete()
}

class TestSingle : UseCase<NoParams, Single<String>> {

    override fun invoke(params: NoParams): Single<String> = Single.just("")
}

interface TestContract {

    interface ViewModel : BaseContract.ViewModel {
        fun testInputForCompletable(input: String)
        fun testInputForSingle()

        fun testOutput(): LiveData<String>
    }
}

class TestViewModel(
    private val testCompletable: TestCompletable,
    private val testSingle: TestSingle,
    getErrorType: GetErrorType,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorType
), TestContract.ViewModel {

    private val testOutput = MutableLiveData<String>()

    override fun testInputForCompletable(input: String) {
        testCompletable(NoParams)
            .compose(observeOnUIAfterCompletableResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle({ testOutput.postValue(input) }, ::postErrorMessage)
    }

    override fun testInputForSingle() {
        testSingle(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle(testOutput::postValue, ::postErrorMessage)
    }

    override fun testOutput(): LiveData<String> = testOutput
}

class BaseViewModelTest : ViewModelTest<TestContract.ViewModel>() {

    private val testCompletable = mockk<TestCompletable>()
    private val testSingle = mockk<TestSingle>()

    override lateinit var viewModel: TestContract.ViewModel

    override fun setupSubject() {
        viewModel = TestViewModel(
            testCompletable,
            testSingle,
            getErrorType,
            schedulerProvider
        )
    }

    @Test
    fun `should show and hide loading and post default error message after failing to test completable`() {
        val error = IllegalArgumentException()
        val params = GetErrorTypeParams(error)

        every { testCompletable(NoParams) } returns Completable.error(error)
        every { getErrorType(params) } returns ErrorType.DEFAULT

        viewModel.testInputForCompletable("")

        assertTrue(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, R.string.error_message_default)

        assertFalse(viewModel.isLoading().value!!)

        verify {
            testCompletable(NoParams)
            getErrorType(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorType)
    }

    @Test
    fun `should show and hide loading and post test output after testing completable successfully`() {
        val message = "message"

        every { testCompletable(NoParams) } returns Completable.complete()

        viewModel.testInputForCompletable(message)

        assertTrue(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.error().value)
        assertEquals(viewModel.testOutput().value, message)

        assertFalse(viewModel.isLoading().value!!)

        verify {
            testCompletable(NoParams)
        }
        confirmVerified(testCompletable, testSingle, getErrorType)
    }

    @Test
    fun `should show and hide loading and post default error message after failing to test single`() {
        val error = IllegalArgumentException()
        val params = GetErrorTypeParams(error)

        every { testSingle(NoParams) } returns Single.error(error)
        every { getErrorType(params) } returns ErrorType.DEFAULT

        viewModel.testInputForSingle()

        assertTrue(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, R.string.error_message_default)

        assertFalse(viewModel.isLoading().value!!)

        verify {
            testSingle(NoParams)
            getErrorType(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorType)
    }

    @Test
    fun `should show and hide loading and post no connection error message after failing to test single`() {
        val error = IllegalArgumentException()
        val params = GetErrorTypeParams(error)

        every { testSingle(NoParams) } returns Single.error(error)
        every { getErrorType(params) } returns ErrorType.NO_CONNECTION

        viewModel.testInputForSingle()

        assertTrue(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, R.string.error_message_no_connection)

        assertFalse(viewModel.isLoading().value!!)

        verify {
            testSingle(NoParams)
            getErrorType(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorType)
    }

    @Test
    fun `should show and hide loading and post test output after testing single successfully`() {
        val message = "message"

        every { testSingle(NoParams) } returns Single.just(message)

        viewModel.testInputForSingle()

        assertTrue(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.error().value)
        assertEquals(viewModel.testOutput().value, message)

        assertFalse(viewModel.isLoading().value!!)

        verify {
            testSingle(NoParams)
        }
        confirmVerified(testCompletable, testSingle, getErrorType)
    }
}
