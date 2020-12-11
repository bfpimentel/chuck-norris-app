package dev.pimentel.data.sources.remote

import retrofit2.http.GET

internal interface CategoriesRemoteDataSource {

    @GET("/jokes/categories")
    suspend fun getAllCategoriesNames(): List<String>
}
