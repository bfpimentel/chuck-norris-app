package dev.pimentel.chucknorris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dev.pimentel.chucknorris.databinding.MainActivityBinding
import dev.pimentel.chucknorris.di.appModules
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.shared.navigator.NavigatorBinder
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    private val navigator: NavigatorBinder by inject<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            loadKoinModules(appModules)
        }

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!
            .findNavController()

        navigator.bind(navController)
    }

    override fun onPause() {
        super.onPause()
        navigator.unbind()
    }
}
