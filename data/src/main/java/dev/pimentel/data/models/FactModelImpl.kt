package dev.pimentel.data.models

import dev.pimentel.domain.models.FactModel

internal data class FactModelImpl(
    override val id: String,
    override val categories: List<String>,
    override val url: String,
    override val value: String
) : FactModel
