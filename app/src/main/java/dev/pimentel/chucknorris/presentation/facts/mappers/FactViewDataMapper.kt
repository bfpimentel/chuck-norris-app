package dev.pimentel.chucknorris.presentation.facts.mappers

import android.content.Context
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactViewData
import dev.pimentel.domain.entities.Fact

interface FactViewDataMapper {
    fun map(facts: List<Fact>): List<FactViewData>
}

class FactViewDataMapperImpl(private val context: Context) : FactViewDataMapper {

    override fun map(facts: List<Fact>): List<FactViewData> = facts.map { fact ->
        FactViewData(
            fact.id,
            fact.category?.capitalize() ?: context.getString(R.string.get_facts_no_category),
            fact.value,
            if (fact.value.length > SMALL_FONT_LENGTH_LIMIT) R.dimen.text_normal
            else R.dimen.text_large
        )
    }

    private companion object {
        const val SMALL_FONT_LENGTH_LIMIT = 80
    }
}
