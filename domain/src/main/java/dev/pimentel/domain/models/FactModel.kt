package dev.pimentel.domain.models

interface FactModel {
    val id: String
    val categories: List<String>
    val url: String
    val value: String
}
