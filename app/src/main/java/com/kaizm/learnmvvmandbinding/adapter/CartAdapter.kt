package com.kaizm.learnmvvmandbinding.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.databinding.ItemCartBinding

interface CartOnClick {
    fun onClickDelete(cart: Cart)
    fun oClickPlus(cart: Cart)
    fun onClickMinus(cart: Cart)
}

class CartAdapter(
    private val onClick: CartOnClick
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var list: List<Cart>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    private val differCallback = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart) {
            Glide.with(binding.img).load(cart.thumbnail).into(binding.img)
            binding.price.text = "$${cart.price}"
            binding.title.text = cart.title
            binding.quantity.text = cart.quantity.toString()
            binding.plus.setOnClickListener {
                onClick.oClickPlus(cart)
            }
            binding.minus.setOnClickListener {
                onClick.onClickMinus(cart)
            }

            binding.delete.setOnClickListener {
                onClick.onClickDelete(cart)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = list[position]
        holder.bind(cart)
    }

    override fun getItemCount(): Int = list.size
}
