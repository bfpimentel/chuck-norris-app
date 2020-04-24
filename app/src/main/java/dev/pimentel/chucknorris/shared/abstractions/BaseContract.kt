package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.LiveData

interface BaseContract {

    interface ViewModel {
        fun isLoading(): LiveData<Unit>
        fun isNotLoading(): LiveData<Unit>
        fun error(): LiveData<String>
    }
}
