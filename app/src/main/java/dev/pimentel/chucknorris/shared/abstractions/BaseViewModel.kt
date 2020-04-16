package dev.pimentel.chucknorris.shared.abstractions

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.ErrorType
import dev.pimentel.domain.usecases.GetErrorType
import dev.pimentel.domain.usecases.GetErrorTypeParams
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    private val schedulerProvider: SchedulerProvider? = null,
    private val getErrorType: GetErrorType? = null
) : ViewModel(),
    BaseContract.ViewModel {

    private val compositeDisposable = CompositeDisposable()

    private val error = MutableLiveData<@StringRes Int>()
    protected val isLoading = MutableLiveData<Boolean>()

    override fun error(): LiveData<Int> = error

    override fun isLoading(): LiveData<Boolean> = isLoading

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun postErrorMessage(throwable: Throwable) {
        getErrorType!!(GetErrorTypeParams(throwable)).let { errorType ->
            when (errorType) {
                ErrorType.NO_CONNECTION -> R.string.error_message_no_connection
                ErrorType.DEFAULT -> R.string.error_message_default
            }
        }.also(error::postValue)
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
