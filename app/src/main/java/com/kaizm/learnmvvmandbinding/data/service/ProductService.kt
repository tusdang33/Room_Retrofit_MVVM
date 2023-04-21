package com.kaizm.learnmvvmandbinding.data.service

import com.kaizm.learnmvvmandbinding.data.api.ProductResponse
import com.kaizm.learnmvvmandbinding.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("/products")
    suspend fun getProduct(): ProductResponse

    @GET("/products/{id}")
    suspend fun getSingleProduct(@Path("id") id: Int): Product
}