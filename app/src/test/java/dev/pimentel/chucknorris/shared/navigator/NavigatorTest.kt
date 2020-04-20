package dev.pimentel.chucknorris.shared.navigator

import androidx.navigation.NavController
import dev.pimentel.chucknorris.R
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NavigatorTest {

    private lateinit var navigator: Navigator

    @BeforeEach
    @Test
    fun setupSubject() {
        navigator = NavigatorImpl()

        assertNotNull(navigator)
    }

    @Test
    fun `should bind navigator and navigate to facts`() {
        val navController = mockk<NavController>(relaxed = true)
        val destinationId = R.id.facts_fragment

        every { navController.navigate(destinationId) } just runs

        navigator.bind(navController)
        navigator.navigate(destinationId)

        verify { navController.navigate(destinationId) }
        confirmVerified(navController)
    }

    @Test
    fun `should bind navigator and pop`() {
        val navController = mockk<NavController>(relaxed = true)

        every { navController.popBackStack() } returns true

        navigator.bind(navController)
        navigator.pop()

        verify { navController.popBackStack() }
        confirmVerified(navController)
    }

    @Test
    fun `should unbind navigator and doi nothing when trying to navigate to facts`() {
        val navController = mockk<NavController>(relaxed = true)
        val destinationId = R.id.facts_fragment

        navigator.bind(navController)
        navigator.unbind()
        navigator.navigate(destinationId)

        confirmVerified(navController)
    }
}
