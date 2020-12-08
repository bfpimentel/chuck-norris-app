package dev.pimentel.chucknorris.shared.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.lifecycleBinding(bindingFactory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        init {
            this@lifecycleBinding
                .viewLifecycleOwnerLiveData
                .observe(this@lifecycleBinding, Observer { owner ->
                    owner?.lifecycle?.addObserver(this)
                })
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return binding ?: bindingFactory(requireView()).also { newBinding ->
                binding = newBinding
            }
        }
    }

inline fun <T> Fragment.watch(source: StateFlow<T>, crossinline block: (T) -> Unit) {
    lifecycleScope.launchWhenStarted { source.collect { block(it) } }
}
