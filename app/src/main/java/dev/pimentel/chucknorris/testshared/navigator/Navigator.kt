package dev.pimentel.chucknorris.testshared.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.navOptions

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    fun navigate(
        @IdRes destinationId: Int
    )
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl : Navigator {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun navigate(@IdRes destinationId: Int) {
        val navOptions = navOptions {
            anim {
                enter = android.R.anim.fade_in
                exit = android.R.anim.fade_out
                popEnter = android.R.anim.fade_in
                popExit = android.R.anim.fade_out
            }
        }

        navController?.navigate(destinationId, null, navOptions)
    }
}
