package dev.pimentel.data.sources

import dev.pimentel.data.models.Fact
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FactsRemoteDataSource {

    @GET("/jokes/search")
    fun getFacts(@Query("query") searchTerm: String): Single<List<Fact>>
}
