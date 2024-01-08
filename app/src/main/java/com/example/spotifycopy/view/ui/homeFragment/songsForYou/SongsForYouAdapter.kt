package com.example.spotifycopy.view.ui.homeFragment.songsForYou

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.SongsForYouItemBinding
import com.example.spotifycopy.presentation.models.CartItemModel
import com.example.spotifycopy.presentation.models.SongModel

class SongsForYouAdapter : RecyclerView.Adapter<SongsForYouAdapter.SongsForYouViewHolder>() {

    private val songsForYouCallBack = object : DiffUtil.ItemCallback<SongModel>() {
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, songsForYouCallBack)

    inner class SongsForYouViewHolder(private val binding : SongsForYouItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SongModel){
            binding.txtSongName.text = item.title
            binding.txtArtistName.text = item.artist
            Glide.with(itemView.context).load(item.imageUrl).into(binding.imgSong)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsForYouViewHolder {
        val binding = SongsForYouItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SongsForYouViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: SongsForYouViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addItems(songsForYou : List<SongModel>) = diffUtil.submitList(songsForYou)

}