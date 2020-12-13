package dev.pimentel.chucknorris.presentation.facts.data

sealed class FactsIntention {

    object GetLastSearchAndFacts : FactsIntention()

    data class NewSearch(val term: String) : FactsIntention()

    object NavigateToSearch : FactsIntention()

    data class ShareFact(val id: String) : FactsIntention()
}
