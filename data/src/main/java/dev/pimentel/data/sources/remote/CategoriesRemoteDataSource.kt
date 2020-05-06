package dev.pimentel.data.sources.remote

import io.reactivex.Single
import retrofit2.http.GET

interface CategoriesRemoteDataSource {

    @GET("/jokes/categories")
    fun getAllCategoriesNames(): Single<List<String>>
}