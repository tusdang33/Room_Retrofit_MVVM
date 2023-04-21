package com.kaizm.learnmvvmandbinding.data.api

import com.google.gson.annotations.SerializedName
import com.kaizm.learnmvvmandbinding.data.model.Product

data class ProductResponse(
    val limit: Int,
    @SerializedName("products") val products: List<Product>,
    val skip: Int,
    val total: Int
)