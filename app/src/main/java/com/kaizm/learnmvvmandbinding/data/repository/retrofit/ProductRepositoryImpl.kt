package com.kaizm.learnmvvmandbinding.data.repository.retrofit

import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.data.service.ProductService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productService: ProductService) :
    ProductRepository {
    override suspend fun fetchData(): Result<List<Product>> {
        return try {
            val response = productService.getProduct().products
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchSingleData(int: Int): Result<List<Product>> {
        return try {
            val response = productService.getSingleProduct(int)
            Result.success(listOf(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}