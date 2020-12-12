package dev.pimentel.chucknorris.presentation.facts.mappers

import dev.pimentel.chucknorris.presentation.facts.data.ShareableFact
import dev.pimentel.domain.entities.Fact

interface ShareableFactMapper {
    fun map(fact: Fact): ShareableFact
}

class ShareableFactMapperImpl : ShareableFactMapper {

    override fun map(fact: Fact): ShareableFact = ShareableFact(
        fact.url,
        fact.value
    )
}
