package dev.pimentel.data.models

data class FactsResponse(
    val result: List<Fact>
) {

    data class Fact(
        val categories: List<String>,
        val url: String,
        val value: String
    )
}
