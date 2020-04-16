package dev.pimentel.chucknorris.shared.abstractions

import androidx.lifecycle.LiveData

interface BaseContract {

    interface ViewModel {
        fun isLoading(): LiveData<Boolean>
        fun error(): LiveData<Int>
    }
}
