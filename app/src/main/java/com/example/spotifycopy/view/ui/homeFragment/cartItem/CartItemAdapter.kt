package com.example.spotifycopy.view.ui.homeFragment.cartItem

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.CartItemBinding
import com.example.spotifycopy.presentation.models.CartItemModel

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private val cartItemCallBack = object : DiffUtil.ItemCallback<CartItemModel>() {
        override fun areItemsTheSame(oldItem: CartItemModel, newItem: CartItemModel): Boolean {
            return oldItem.titleSong == newItem.titleSong
        }

        override fun areContentsTheSame(oldItem: CartItemModel, newItem: CartItemModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, cartItemCallBack)

    inner class CartItemViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CartItemModel) {
            binding.txtName.text = item.titleSong
            Glide.with(itemView.context).load(item.imageSong).into(binding.cardImage)
            binding.txtName.text = item.titlePlaylist
            Glide.with(itemView.context).load(item.imagePlaylist).into(binding.cardImage)
            Log.e("item",item.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addItems(cart : List<CartItemModel>) = diffUtil.submitList(cart)

}