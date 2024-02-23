package com.example.spotifycopy.view.ui.libraryFragment.searchInLibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.SearchLibraryItemBinding
import com.example.spotifycopy.domain.models.ArtistsModel
import com.example.spotifycopy.domain.models.LibraryModel

class SearchInLibraryAdapter : RecyclerView.Adapter<SearchInLibraryAdapter.SearchViewHolder>() {

    private val libraryCallBack = object : DiffUtil.ItemCallback<LibraryModel>() {
        override fun areItemsTheSame(oldItem: LibraryModel, newItem: LibraryModel): Boolean {
            return oldItem.artistName == newItem.artistName
        }

        override fun areContentsTheSame(oldItem: LibraryModel, newItem: LibraryModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, libraryCallBack)


    inner class SearchViewHolder(private val binding: SearchLibraryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LibraryModel) {
//         if(item.playlistName.isNotEmpty()) {
//             binding.txtName.text = item.playlistName
//             binding.txtCategory.text = "Playlist"
//             Glide.with(itemView.context).load(item.imgPlaylist).into(binding.circleImageView)
//         }else {
            binding.txtName.text = item.artistName
            binding.txtCategory.text = "Artist"
            Glide.with(itemView.context).load(item.imageUrl).into(binding.circleImageView)
            //  }
            //}
        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val binding =
                SearchLibraryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SearchViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return diffUtil.currentList.size
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.bind(diffUtil.currentList[position])
        }

        fun addItems(items: List<LibraryModel>) = diffUtil.submitList(items)

    }