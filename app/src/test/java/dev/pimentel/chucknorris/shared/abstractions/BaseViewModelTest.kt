package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), TestContract.ViewModel {

    private val testOutput = MutableLiveData<String>()

    override fun testInputForCompletable(input: String) {
        testCompletable(NoParams)
            .compose(observeOnUIAfterCompletableResult())
            .doOnSubscribe { isLoading.postValue(Unit) }
            .doFinally { isNotLoading.postValue(Unit) }
            .handle({ testOutput.postValue(input) }, ::postErrorMessage)
    }

    override fun testInputForSingle() {
        testSingle(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(Unit) }
            .doFinally { isNotLoading.postValue(Unit) }
            .handle(testOutput::postValue, ::postErrorMessage)
    }

    override fun testOutput(): LiveData<String> = testOutput
}

class BaseViewModelTest : ViewModelTest<TestContract.ViewModel>() {

    private val testCompletable = mockk<TestCompletable>()
    private val testSingle = mockk<TestSingle>()

    override lateinit var viewModel: TestContract.ViewModel

    override fun `setup subject`() {
        viewModel = TestViewModel(
            testCompletable,
            testSingle,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    fun `should show and hide loading and post default error message after failing to test completable`() {
        val error = IllegalArgumentException()
        val params = GetErrorMessage.Params(error)
        val message = "message"

        every { testCompletable(NoParams) } returns Completable.error(error)
        every { getErrorMessage(params) } returns message

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.testInputForCompletable("")

        assertNotNull(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, message)

        assertNotNull(viewModel.isLoading().value!!)

        verify(exactly = 1) {
            testCompletable(NoParams)
            getErrorMessage(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorMessage)
    }

    @Test
    fun `should show and hide loading and post test output after testing completable successfully`() {
        val message = "message"

        every { testCompletable(NoParams) } returns Completable.complete()

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.testInputForCompletable(message)

        assertNotNull(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.error().value)
        assertEquals(viewModel.testOutput().value, message)

        assertNotNull(viewModel.isNotLoading().value!!)

        verify(exactly = 1) {
            testCompletable(NoParams)
        }
        confirmVerified(testCompletable, testSingle, getErrorMessage)
    }

    @Test
    fun `should show and hide loading and post default error message after failing to test single`() {
        val error = IllegalArgumentException()
        val params = GetErrorMessage.Params(error)
        val message = "message"

        every { testSingle(NoParams) } returns Single.error(error)
        every { getErrorMessage(params) } returns message

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.testInputForSingle()

        assertNotNull(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, message)

        assertNotNull(viewModel.isNotLoading().value!!)

        verify(exactly = 1) {
            testSingle(NoParams)
            getErrorMessage(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorMessage)
    }

    @Test
    fun `should show and hide loading and post no connection error message after failing to test single`() {
        val error = IllegalArgumentException()
        val params = GetErrorMessage.Params(error)
        val message = "message"

        every { testSingle(NoParams) } returns Single.error(error)
        every { getErrorMessage(params) } returns message

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.testInputForSingle()

        assertNotNull(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.testOutput().value)
        assertEquals(viewModel.error().value, message)

        assertNotNull(viewModel.isNotLoading().value!!)

        verify(exactly = 1) {
            testSingle(NoParams)
            getErrorMessage(params)
        }
        confirmVerified(testCompletable, testSingle, getErrorMessage)
    }

    @Test
    fun `should show and hide loading and post test output after testing single successfully`() {
        val message = "message"

        every { testSingle(NoParams) } returns Single.just(message)

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.testInputForSingle()

        assertNotNull(viewModel.isLoading().value!!)

        testScheduler.triggerActions()

        assertNull(viewModel.error().value)
        assertEquals(viewModel.testOutput().value, message)

        assertNotNull(viewModel.isNotLoading().value!!)

        verify(exactly = 1) {
            testSingle(NoParams)
        }
        confirmVerified(testCompletable, testSingle, getErrorMessage)
    }
}
