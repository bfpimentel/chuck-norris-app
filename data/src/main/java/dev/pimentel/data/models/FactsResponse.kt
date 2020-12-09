package dev.pimentel.data.models

import com.squareup.moshi.Json

data class FactsResponse(
    @Json(name = "result") val result: List<Fact>
) {

    data class Fact(
        @Json(name = "id") val id: String,
        @Json(name = "categories") val categories: List<String>,
        @Json(name = "url") val url: String,
        @Json(name = "value") val value: String
    )
}
