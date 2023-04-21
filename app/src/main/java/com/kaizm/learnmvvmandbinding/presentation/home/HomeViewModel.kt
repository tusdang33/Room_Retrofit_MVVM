package com.kaizm.learnmvvmandbinding.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kaizm.learnmvvmandbinding.adapter.ProductPagination
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.data.repository.retrofit.ProductRepository
import com.kaizm.learnmvvmandbinding.data.repository.room.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "AAA"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository, private val cartRepository: CartRepository
) : ViewModel() {
    sealed class Event {
        data class Success(val message: String) : Event()
        data class Fail(val message: String) : Event()
    }

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    private var _listProduct = MutableStateFlow<List<Product>>(listOf())
    val listProduct: StateFlow<List<Product>> = _listProduct

    val productList = Pager(PagingConfig(3)) {
        ProductPagination(productRepository)
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.fetchData().fold(onSuccess = {
                _listProduct.value = it
            }, onFailure = {
                Log.e(TAG, "Get product fail ${it.localizedMessage}")
            })
        }
    }

    fun addToCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            if (cartRepository.isItemExist(cart.id)) {
                _event.trySend(Event.Fail(cart.title))
            } else {
                cartRepository.insertData(cart)
                _event.trySend(Event.Success(cart.title))
            }
        }
    }
}