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
) : Fragment(layoutResId), ViewBindingHolder<BindingType> by ViewBindingHolderImpl()
        where ViewModelType : BaseContract.ViewModel,
              BindingType : ViewBinding {

    abstract val module: Module
    abstract val viewModel: ViewModelType

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
        unbindView()
    }

    abstract fun bindView(): View

    protected inline fun <ObserverType> LiveData<ObserverType>.observe(
        crossinline observer: (ObserverType) -> Unit
    ) = observe(viewLifecycleOwner, Observer { observer(it) })
}
