package com.kaizm.learnmvvmandbinding.data.service

import androidx.room.*
import com.kaizm.learnmvvmandbinding.data.model.Cart

@Dao
interface DatabaseService {
    @Query("SELECT * FROM cart ORDER BY id DESC")
    fun getAllCart(): List<Cart>

    @Query("SELECT * FROM cart WHERE id = :id")
    fun getCartByID(id: Int): Cart?

    @Insert
    fun addCart(cart: Cart)

    @Delete
    fun deleteCart(cart: Cart)

    @Update
    fun updateCart(cart: Cart)


}