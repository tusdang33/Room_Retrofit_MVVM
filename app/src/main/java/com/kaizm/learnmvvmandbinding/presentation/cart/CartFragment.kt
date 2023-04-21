package com.kaizm.learnmvvmandbinding.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kaizm.learnmvvmandbinding.adapter.CartAdapter
import com.kaizm.learnmvvmandbinding.adapter.CartOnClick
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(object : CartOnClick {
            override fun onClickDelete(cart: Cart) {
                viewModel.deleteCart(cart)
            }

            override fun oClickPlus(cart: Cart) {
                viewModel.plusCart(cart)
            }

            override fun onClickMinus(cart: Cart) {
                viewModel.minusCart(cart)

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.HORIZONTAL
                )
            )
        }

        lifecycleScope.launch {
            viewModel.cartList.collect {
                cartAdapter.list = it
            }
        }

        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when(event) {
                    is CartViewModel.Event.Success -> {
                        Snackbar.make(
                            requireView(), "Delete ${event.message} Done", Snackbar.LENGTH_SHORT
                        ).show()

                    }
                    is CartViewModel.Event.Update -> {
                        viewModel.getData()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}