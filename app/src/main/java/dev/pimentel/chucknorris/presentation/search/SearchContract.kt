package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.presentation.search.data.SearchIntention
import dev.pimentel.chucknorris.presentation.search.data.SearchState
import kotlinx.coroutines.flow.StateFlow

interface SearchContract {

    interface ViewModel {
        fun publish(intention: SearchIntention)
        fun state(): StateFlow<SearchState>
    }
}
