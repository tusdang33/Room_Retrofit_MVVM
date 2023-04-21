package com.kaizm.learnmvvmandbinding.data.repository.retrofit

import com.kaizm.learnmvvmandbinding.data.model.Product

interface ProductRepository {
    suspend fun fetchData(): Result<List<Product>>
    suspend fun fetchSingleData(int: Int): Result<List<Product>>
}