package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class RxViewModel(
    private val schedulerProvider: SchedulerProvider
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
        SingleTransformer<T, T> {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }

    protected fun observeOnUIAfterCompletableResult() =
        CompletableTransformer {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }
}
