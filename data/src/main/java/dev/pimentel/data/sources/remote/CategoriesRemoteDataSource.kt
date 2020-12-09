package dev.pimentel.data.sources.remote

import retrofit2.http.GET

interface CategoriesRemoteDataSource {

    @GET("/jokes/categories")
    fun getAllCategoriesNames(): List<String>
}
