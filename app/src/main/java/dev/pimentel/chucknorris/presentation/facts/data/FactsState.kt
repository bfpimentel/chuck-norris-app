package dev.pimentel.chucknorris.presentation.facts.data

import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.mvi.Event

@Suppress("LongParameterList")
data class FactsState(
    val facts: List<FactDisplay>,
    val searchTerm: String,
    val isFirstAccess: Boolean,
    val isLoading: Boolean,
    val emptyListEvent: Event.NoContent?,
    val shareFactEvent: Event<ShareableFact>?,
    val errorEvent: Event<String>?,
) {

    companion object {
        @JvmStatic
        val INITIAL = FactsState(
            facts = emptyList(),
            searchTerm = "",
            isFirstAccess = false,
            isLoading = false,
            emptyListEvent = null,
            shareFactEvent = null,
            errorEvent = null
        )
    }
}
