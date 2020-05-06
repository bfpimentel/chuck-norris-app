package dev.pimentel.domain.models

data class FactsResponse(
    val result: List<Fact>
) {

    data class Fact(
        val id: String,
        val categories: List<String>,
        val url: String,
        val value: String
    )
}
