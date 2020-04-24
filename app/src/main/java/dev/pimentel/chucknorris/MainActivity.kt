package dev.pimentel.chucknorris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dev.pimentel.chucknorris.databinding.MainLayoutBinding
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.shared.navigator.NavigatorBinder
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigator: NavigatorBinder by inject<Navigator>()

    private lateinit var binding: MainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
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
