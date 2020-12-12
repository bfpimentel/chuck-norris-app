package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import kotlinx.coroutines.flow.StateFlow

interface FactsContract {

    interface ViewModel {
        fun publish(intention: FactsIntention)
        fun state(): StateFlow<FactsState>
    }
}
