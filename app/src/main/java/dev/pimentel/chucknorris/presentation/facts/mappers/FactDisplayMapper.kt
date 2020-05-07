package dev.pimentel.chucknorris.presentation.facts.mappers

import android.content.Context
import androidx.annotation.DimenRes
import dev.pimentel.chucknorris.R
import dev.pimentel.domain.entities.Fact

interface FactDisplayMapper {
    fun map(facts: List<Fact>): List<FactDisplay>
}

class FactDisplayMapperImpl(
    private val context: Context
) : FactDisplayMapper {

    override fun map(facts: List<Fact>): List<FactDisplay> =
        facts.map { fact ->
            FactDisplay(
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

data class FactDisplay(
    val id: String,
    val category: String,
    val value: String,
    @DimenRes val fontSize: Int
)
