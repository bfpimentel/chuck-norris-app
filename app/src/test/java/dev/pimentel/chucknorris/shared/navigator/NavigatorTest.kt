package dev.pimentel.chucknorris.shared.navigator

import androidx.navigation.NavController
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.chucknorris.testshared.TestDispatchersProvider
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NavigatorTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatchersProvider: DispatchersProvider = TestDispatchersProvider(
        testCoroutineDispatcher
    )
    private lateinit var navigator: Navigator

    @BeforeEach
    fun `setup subject`() {
        navigator = NavigatorImpl(dispatchersProvider)
    }

    @Test
    fun `should bind navigator and navigate`() = testCoroutineDispatcher.runBlockingTest {
        val navController = mockk<NavController>(relaxed = true)
        val destinationId = R.id.facts_fragment

        every { navController.navigate(destinationId) } just runs

        navigator.bind(navController)
        navigator.navigate(destinationId)

        verify { navController.navigate(destinationId) }
        confirmVerified(navController)
    }

    @Test
    fun `should unbind navigator and do nothing when trying to navigate`() =
        testCoroutineDispatcher.runBlockingTest {
            val navController = mockk<NavController>(relaxed = true)
            val destinationId = R.id.facts_fragment

            navigator.bind(navController)
            navigator.unbind()
            navigator.navigate(destinationId)

            confirmVerified(navController)
        }

    @Test
    fun `should bind navigator and pop`() = testCoroutineDispatcher.runBlockingTest {
        val navController = mockk<NavController>(relaxed = true)

        every { navController.popBackStack() } returns true

        navigator.bind(navController)
        navigator.pop()

        verify { navController.popBackStack() }
        confirmVerified(navController)
    }

    @Test
    fun `should unbind navigator and do nothing when trying to pop`() =
        testCoroutineDispatcher.runBlockingTest {
            val navController = mockk<NavController>(relaxed = true)

            navigator.bind(navController)
            navigator.unbind()
            navigator.pop()

            confirmVerified(navController)
        }
}
