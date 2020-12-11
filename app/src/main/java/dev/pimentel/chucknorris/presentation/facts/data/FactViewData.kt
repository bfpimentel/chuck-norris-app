package dev.pimentel.chucknorris.presentation.facts.data

import androidx.annotation.DimenRes

data class FactViewData(
    val id: String,
    val category: String,
    val value: String,
    @DimenRes val fontSize: Int
)
