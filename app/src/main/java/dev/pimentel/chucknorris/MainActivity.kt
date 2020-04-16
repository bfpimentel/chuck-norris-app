package dev.pimentel.chucknorris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dev.pimentel.chucknorris.databinding.MainActivityLayoutBinding
import dev.pimentel.chucknorris.testshared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.navigator.NavigatorBinder
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigator: NavigatorBinder by inject<Navigator>()

    private lateinit var binding: MainActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
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
