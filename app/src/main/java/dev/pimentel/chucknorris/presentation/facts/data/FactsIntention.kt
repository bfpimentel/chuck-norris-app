package dev.pimentel.chucknorris.presentation.facts.data

sealed class FactsIntention {

    object GetSearchTermsAndFacts : FactsIntention()

    object NavigateToSearch : FactsIntention()

    data class ShareFact(val id: String) : FactsIntention()
}