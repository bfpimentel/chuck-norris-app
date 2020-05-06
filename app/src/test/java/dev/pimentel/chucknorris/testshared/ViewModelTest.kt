package dev.pimentel.chucknorris.testshared

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import dev.pimentel.chucknorris.shared.abstractions.BaseContract
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import io.mockk.mockk
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true

                override fun postToMainThread(runnable: Runnable) = runnable.run()
            })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

}

@ExtendWith(InstantExecutorExtension::class)
abstract class ViewModelTest<ViewModelType : BaseContract.ViewModel> {

    protected lateinit var testScheduler: TestScheduler
    protected lateinit var schedulerProvider: SchedulerProvider
    protected lateinit var getErrorMessage: GetErrorMessage

    abstract val viewModel: ViewModelType

    abstract fun `setup subject`()

    @Test
    @BeforeEach
    fun `should setup subject and then the test dependencies must not be null`() {
        testScheduler = TestScheduler()
        schedulerProvider = TestSchedulerProvider(testScheduler)
        getErrorMessage = mockk()

        `setup subject`()

        assertNotNull(viewModel)
        assertNotNull(testScheduler)
        assertNotNull(getErrorMessage)
    }
}
