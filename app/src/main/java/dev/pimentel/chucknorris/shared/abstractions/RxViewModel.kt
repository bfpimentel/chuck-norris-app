package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class RxViewModel(
    private val schedulerProvider: SchedulerProvider? = null
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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
        if (schedulerProvider == null) error("schedulerProvider must not be null")
        else {
            SingleTransformer<T, T> {
                it.subscribeOn(schedulerProvider.io)
                    .observeOn(schedulerProvider.ui)
            }
        }

    protected fun observeOnUIAfterCompletableResult() =
        if (schedulerProvider == null) error("schedulerProvider must not be null")
        else {
            CompletableTransformer {
                it.subscribeOn(schedulerProvider.io)
                    .observeOn(schedulerProvider.ui)
            }
        }
}
