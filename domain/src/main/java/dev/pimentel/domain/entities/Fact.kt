package dev.pimentel.domain.entities

data class Fact(
    val categories: List<String>,
    val url: String,
    val value: String
)
