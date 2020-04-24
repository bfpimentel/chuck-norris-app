package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorMessage
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    private val schedulerProvider: SchedulerProvider? = null,
    private val getErrorMessage: GetErrorMessage? = null
) : ViewModel(), BaseContract.ViewModel {

    private val compositeDisposable = CompositeDisposable()

    private val error = MutableLiveData<String>()
    protected val isLoading = MutableLiveData<Unit>()
    protected val isNotLoading = MutableLiveData<Unit>()

    final override fun error(): LiveData<String> = error
    final override fun isLoading(): LiveData<Unit> = isLoading
    final override fun isNotLoading(): LiveData<Unit> = isNotLoading

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun postErrorMessage(throwable: Throwable) {
        getErrorMessage!!(GetErrorMessage.Params(throwable))
            .also(error::postValue)
    }

    protected fun <T> Single<T>.handle(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = compositeDisposable.add(this.subscribe(onSuccess, onError))

    protected fun Completable.handle(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) = compositeDisposable.add(this.subscribe(onSuccess, onError))

    protected fun <T> observeOnUIAfterSingleResult() =
        SingleTransformer<T, T> {
            it.subscribeOn(schedulerProvider!!.io)
                .observeOn(schedulerProvider.ui)
        }

    protected fun observeOnUIAfterCompletableResult() =
        CompletableTransformer {
            it.subscribeOn(schedulerProvider!!.io)
                .observeOn(schedulerProvider.ui)
        }
}
