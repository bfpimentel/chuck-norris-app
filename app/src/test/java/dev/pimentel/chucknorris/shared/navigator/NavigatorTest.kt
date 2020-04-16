package dev.pimentel.chucknorris.shared.navigator

import androidx.navigation.NavController
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.testshared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.navigator.NavigatorImpl
import io.mockk.*
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

        every { navController.navigate(destinationId, null, any()) } just runs

        navigator.bind(navController)
        navigator.navigate(destinationId)

        verify { navController.navigate(destinationId, null, any()) }
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
