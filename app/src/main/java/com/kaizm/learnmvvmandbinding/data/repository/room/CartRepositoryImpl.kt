package com.kaizm.learnmvvmandbinding.data.repository.room

import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.service.DatabaseService
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val appDAO: DatabaseService) : CartRepository {
    override suspend fun fetchData(): List<Cart> {
        return appDAO.getAllCart()
    }

    override suspend fun insertData(cart: Cart) {
        appDAO.addCart(cart)
    }

    override suspend fun isItemExist(id: Int): Boolean {
        appDAO.getCartByID(id)?.let {
            return true
        }
        return false
    }

    override suspend fun deleteData(cart: Cart) {
        appDAO.deleteCart(cart)
    }

    override suspend fun updateCart(cart: Cart) {
        appDAO.updateCart(cart)
    }
}