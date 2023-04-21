package com.kaizm.learnmvvmandbinding.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kaizm.learnmvvmandbinding.common.Const.TAG
import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.data.repository.retrofit.ProductRepository

class ProductPagination(private val productRepository: ProductRepository) :
    PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 1
            var productData = listOf<Product>()
            productRepository.fetchSingleData(currentPage).fold(onSuccess = {
                productData = it
            }, onFailure = {
                Log.e(TAG, "load: ${it.localizedMessage}")
            })
            LoadResult.Page(
                data = productData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}