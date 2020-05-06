package dev.pimentel.chucknorris.presentation.facts

import android.content.Context
import dev.pimentel.chucknorris.R
import dev.pimentel.domain.entities.Fact

interface FactDisplayMapper {
    fun map(facts: List<Fact>): List<FactsViewModel.FactDisplay>
}

class FactDisplayMapperImpl(
    private val context: Context
) : FactDisplayMapper {

    override fun map(facts: List<Fact>): List<FactsViewModel.FactDisplay> =
        facts.map { fact ->
            FactsViewModel.FactDisplay(
                fact.id,
                (fact.category ?: context.getString(R.string.get_facts_no_category)).capitalize(),
                fact.value,
                if (fact.value.length > SMALL_FONT_LENGTH_LIMIT) R.dimen.text_normal
                else R.dimen.text_large
            )
        }

    private companion object {
        const val SMALL_FONT_LENGTH_LIMIT = 80
    }
}
