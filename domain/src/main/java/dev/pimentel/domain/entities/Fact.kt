package dev.pimentel.domain.entities

data class Fact(
    val id: String,
    val category: String,
    val url: String,
    val value: String
)
