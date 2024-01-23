package com.example.spotifycopy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.SwipeItemBinding
import com.example.spotifycopy.domain.models.SongModel

class SwipeSongAdapter(
) : RecyclerView.Adapter<SwipeSongAdapter.SwipeViewHolder>(){
    private var itemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    private val songsCallBack = object : DiffUtil.ItemCallback<SongModel>() {
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, songsCallBack)

    var songs : List<SongModel>
        get() = diffUtil.currentList
        set(value) = diffUtil.submitList(value)

    inner class SwipeViewHolder(private val binding : SwipeItemBinding) : RecyclerView.ViewHolder(binding.root){

        init{
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(item : SongModel){
            binding.songName.text = item.title
            binding.songArtist.text = item.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        val binding = SwipeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SwipeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

}