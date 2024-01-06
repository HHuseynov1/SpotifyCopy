package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.data.entities.Genders
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.databinding.InsidePlaylistItemBinding
import com.example.spotifycopy.presentation.models.SongModel
import com.example.spotifycopy.presentation.models.UserModel

class InsidePlaylistAdapter(
    private val currentSong: (Int) -> Unit
):
    RecyclerView.Adapter<InsidePlaylistAdapter.InsidePlaylistViewHolder>() {

    private val songCallBack = object : DiffUtil.ItemCallback<SongModel>() {
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, songCallBack)

    inner class InsidePlaylistViewHolder(private val binding: InsidePlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                itemView.setOnClickListener {
                    currentSong(bindingAdapterPosition)
                }
            }

        fun bind(item: SongModel) {
            binding.txtArtistName.text = item.artist
            binding.txtSongName.text = item.title
            Glide.with(itemView.context).load(item.imageUrl).into(binding.imgSong)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsidePlaylistViewHolder {
        val binding =
            InsidePlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsidePlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: InsidePlaylistViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addItems(songs: List<SongModel>) = diffUtil.submitList(songs)

}