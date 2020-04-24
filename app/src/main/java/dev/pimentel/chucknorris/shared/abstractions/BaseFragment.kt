package dev.pimentel.chucknorris.shared.abstractions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class BaseFragment<ViewModelType, BindingType>(
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) where ViewModelType : BaseContract.ViewModel,
                                BindingType : ViewBinding {

    private var binding: BindingType? = null

    abstract val module: Module
    abstract val viewModel: ViewModelType

    abstract fun bindView(): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadKoinModules(module)
        return bindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(module)
        binding = null
    }

    protected fun initBinding(
        binding: BindingType,
        onBind: BindingType.() -> Unit
    ): View {
        this.binding = binding
        binding.apply(onBind)
        return binding.root
    }

    protected inline fun <ObserverType> LiveData<ObserverType>.observe(
        crossinline onChanged: (ObserverType) -> Unit
    ) = observe(viewLifecycleOwner, Observer { onChanged(it) })
}
