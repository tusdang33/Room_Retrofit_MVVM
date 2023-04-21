package com.kaizm.learnmvvmandbinding.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaizm.learnmvvmandbinding.data.model.Product
import com.kaizm.learnmvvmandbinding.databinding.ItemProductBinding

class ProductAdapter(private val onClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var list: List<Product>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    private val differCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

//    fun updateList(newList:List<Product>){
//        list.clear()
//        list.addAll(newList)
//        notifyDataSetChanged()
//    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
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

            binding.image.setOnClickListener {
                onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = list[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = list.size

}
