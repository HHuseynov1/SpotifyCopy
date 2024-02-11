package com.example.spotifycopy.view.ui.libraryFragment.playlis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.PlaylistItemForLibraryBinding
import com.example.spotifycopy.domain.models.UserModel

class PlaylistItemAdapter(
    private val insideFragment: () -> Unit,
) : RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>() {

    private val playlistCallBack = object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, playlistCallBack)

    inner class PlaylistItemViewHolder(private val binding: PlaylistItemForLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                insideFragment.invoke()
            }
        }

        fun bind(item: UserModel) {
            binding.txtUserName.text = item.userName
            binding.txtPlaylistName.text = item.playlistName
            Glide.with(itemView.context).load(item.imgPlaylist).into(binding.imgPlaylist)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val binding = PlaylistItemForLibraryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addPlaylist(playlist: List<UserModel>) = diffUtil.submitList(playlist)

}