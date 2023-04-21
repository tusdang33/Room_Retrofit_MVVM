package com.kaizm.learnmvvmandbinding.data.repository.room

import com.kaizm.learnmvvmandbinding.data.model.Cart

interface CartRepository {
    suspend fun fetchData(): List<Cart>
    suspend fun insertData(cart: Cart)
    suspend fun isItemExist(id: Int): Boolean
    suspend fun deleteData(cart: Cart)
    suspend fun updateCart(cart: Cart)
}