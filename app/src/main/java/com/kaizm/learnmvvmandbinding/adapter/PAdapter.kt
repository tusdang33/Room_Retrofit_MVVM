package com.kaizm.learnmvvmandbinding.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.databinding.ItemProductBinding

interface ProductOnClick {
    fun clickAddToCart(cart: Cart)
}

class PAdapter(private val onClick: ProductOnClick) :
    PagingDataAdapter<Product, PAdapter.PViewHolder>(differCallback) {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            Glide.with(binding.image).load(product.thumbnail).into(binding.image)
            binding.discount.text = "- ${product.discountPercentage}%"
            binding.title.text = product.title
            binding.price.text = "$${product.price}"
            binding.priceFake.text =
                "$%.2f".format(product.price * (100 + product.discountPercentage) / 100)
            binding.rating.text = product.rating.toString()

            binding.addCart.setOnClickListener {
                onClick.clickAddToCart(
                    cart = Cart(
                        product.id, product.price, product.thumbnail, product.title, 1
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: PViewHolder, position: Int) {
        val product = getItem(position)
        product?.let { holder.bind(it) }
        holder.setIsRecyclable(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PViewHolder {
        return PViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}