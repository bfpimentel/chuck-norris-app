package dev.pimentel.data.sources.remote

import dev.pimentel.data.dto.FactsResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface FactsRemoteDataSource {

    @GET("/jokes/search")
    suspend fun getFacts(@Query("query") searchTerm: String): FactsResponseDTO
}
