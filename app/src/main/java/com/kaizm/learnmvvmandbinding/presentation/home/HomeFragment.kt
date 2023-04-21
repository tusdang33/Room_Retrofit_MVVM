package com.kaizm.learnmvvmandbinding.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kaizm.learnmvvmandbinding.R
import com.kaizm.learnmvvmandbinding.adapter.PAdapter
import com.kaizm.learnmvvmandbinding.adapter.ProductAdapter
import com.kaizm.learnmvvmandbinding.adapter.ProductOnClick
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter() {
            navigateToDetail(it)
        }
    }

    private val pAdapter: PAdapter by lazy {
        PAdapter(object : ProductOnClick {
            override fun clickAddToCart(cart: Cart) {
                viewModel.addToCart(cart)
            }
        })
    }

    private fun navigateToDetail(product: Product) {
        val bundle = Bundle().apply {
            putSerializable("product", product)
        }
        findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenStarted {
            viewModel.productList.collect {
                pAdapter.submitData(it)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = pAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.HORIZONTAL
                )
            )
        }

        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when(event) {
                    is HomeViewModel.Event.Success -> {
                        Snackbar.make(
                            binding.root, "Add ${event.message} Cart Done", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is HomeViewModel.Event.Fail -> {
                        Snackbar.make(
                            binding.root, "Add ${event.message} Cart Fail", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                Log.e(TAG, "In coroutine")
            }
        }
    }
}