package dev.pimentel.chucknorris.shared.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController
import dev.pimentel.chucknorris.shared.dispatchersprovider.DispatchersProvider
import kotlinx.coroutines.withContext

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    suspend fun navigate(@IdRes destinationId: Int)
    suspend fun pop()
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl(private val dispatchersProvider: DispatchersProvider) : Navigator {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override suspend fun navigate(@IdRes destinationId: Int) {
        withContext(dispatchersProvider.ui) {
            navController?.navigate(destinationId)
        }
    }

    override suspend fun pop() {
        withContext(dispatchersProvider.ui) {
            navController?.popBackStack()
        }
    }
}
