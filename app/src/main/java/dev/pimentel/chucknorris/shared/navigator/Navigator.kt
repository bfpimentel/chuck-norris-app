package dev.pimentel.chucknorris.shared.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController
import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    fun navigate(@IdRes destinationId: Int)
    fun pop()
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

    override fun navigate(@IdRes destinationId: Int) {
        GlobalScope.launch(dispatchersProvider.ui) {
            navController?.navigate(destinationId)
        }
    }

    override fun pop() {
        GlobalScope.launch(dispatchersProvider.ui) {
            navController?.popBackStack()
        }
    }
}
