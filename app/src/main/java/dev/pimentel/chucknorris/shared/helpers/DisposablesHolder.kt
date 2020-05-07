package dev.pimentel.chucknorris.shared.helpers

import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

interface DisposablesHolder {
    fun <T> observeOnUIAfterSingleResult(): SingleTransformer<T, T>
    fun observeOnUIAfterCompletableResult(): CompletableTransformer
    fun <T> Single<T>.handle(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit)
    fun Completable.handle(onSuccess: () -> Unit, onError: (Throwable) -> Unit)
    fun dispose()
}

class DisposablesHolderImpl(
    private val schedulerProvider: SchedulerProvider
) : DisposablesHolder {

    private val compositeDisposable = CompositeDisposable()

    override fun dispose() {
        compositeDisposable.dispose()
    }

    override fun <T> observeOnUIAfterSingleResult() =
        SingleTransformer<T, T> {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }

    override fun observeOnUIAfterCompletableResult() =
        CompletableTransformer {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }

    override fun <T> Single<T>.handle(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(this.subscribe(onSuccess, onError))
    }

    override fun Completable.handle(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(this.subscribe(onSuccess, onError))
    }
}
