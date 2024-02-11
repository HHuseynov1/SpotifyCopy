package com.example.spotifycopy.view.ui.libraryFragment.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.ArtistItemForLibraryBinding
import com.example.spotifycopy.databinding.ArtistsItemBinding
import com.example.spotifycopy.domain.models.ArtistsModel
import com.example.spotifycopy.domain.models.UserModel

class ArtistsItemAdapter : RecyclerView.Adapter<ArtistsItemAdapter.ArtistViewHolder>() {

    private val artistCallBack = object : DiffUtil.ItemCallback<ArtistsModel>() {
        override fun areItemsTheSame(oldItem: ArtistsModel, newItem: ArtistsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArtistsModel, newItem: ArtistsModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, artistCallBack)

    inner class ArtistViewHolder(private val binding : ArtistItemForLibraryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ArtistsModel){
            binding.txtArtistName.text = item.artistName
            Glide.with(itemView.context).load(item.imageUrl).into(binding.circleImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ArtistItemForLibraryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(diffUtil.currentList.size > 5){
            5
        }else{
            diffUtil.currentList.size
        }
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addArtists(artists : List<ArtistsModel>) = diffUtil.submitList(artists)

}