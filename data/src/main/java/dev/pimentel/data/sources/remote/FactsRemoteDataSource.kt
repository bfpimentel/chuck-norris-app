package dev.pimentel.data.sources.remote

import dev.pimentel.data.models.FactsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FactsRemoteDataSource {

    @GET("/jokes/search")
    fun getFacts(@Query("query") searchTerm: String): Single<FactsResponse>
}
