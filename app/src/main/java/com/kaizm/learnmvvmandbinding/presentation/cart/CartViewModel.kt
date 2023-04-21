package com.kaizm.learnmvvmandbinding.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.repository.room.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    sealed class Event() {
        data class Success(val message: String) : Event()
        object Update : Event()
    }

    private val _cartList = MutableStateFlow<List<Cart>>(listOf())
    val cartList: StateFlow<List<Cart>> = _cartList

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _cartList.value = cartRepository.fetchData()
        }
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteData(cart)
            _event.trySend(Event.Success(cart.title))
            _event.trySend(Event.Update)
        }
    }

    fun plusCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.updateCart(cart.copy(quantity = cart.quantity + 1))
            _event.trySend(Event.Update)
        }
    }

    fun minusCart(cart: Cart) {
        if (cart.quantity == 1) {
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                cartRepository.updateCart(cart.copy(quantity = cart.quantity - 1))
                _event.trySend(Event.Update)
            }
        }
    }
}