package com.example.spotifycopy.view.ui.searchFragment.SearchInsideFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.SongsForSearchItemBinding
import com.example.spotifycopy.presentation.models.SongModel

class SearchInsideAdapter : RecyclerView.Adapter<SearchInsideAdapter.SearchInsideViewHolder>() {

    private val songsCallBack = object : DiffUtil.ItemCallback<SongModel>() {
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, songsCallBack)


    inner class SearchInsideViewHolder(private val binding : SongsForSearchItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SongModel){
            binding.txtArtistName.text = item.artist
            binding.txtSongName.text = item.title
            Glide.with(itemView.context).load(item.imageUrl).into(binding.imgSong)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchInsideViewHolder {
        val binding = SongsForSearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchInsideViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: SearchInsideViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addItems(songs : List<SongModel>) = diffUtil.submitList(songs)

}