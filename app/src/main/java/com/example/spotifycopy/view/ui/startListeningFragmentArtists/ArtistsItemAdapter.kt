package com.example.spotifycopy.view.ui.startListeningFragmentArtists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.ArtistsItemBinding
import com.example.spotifycopy.presentation.models.ArtistsModel

class ArtistsItemAdapter : RecyclerView.Adapter<ArtistsItemAdapter.ArtistsViewHolder>() {


    private val genderCallBack = object : DiffUtil.ItemCallback<ArtistsModel>() {
        override fun areItemsTheSame(oldItem: ArtistsModel, newItem: ArtistsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArtistsModel, newItem: ArtistsModel): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, genderCallBack)


    inner class ArtistsViewHolder(private val binding : ArtistsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ArtistsModel){
            binding.txtName.text = item.artistName
            Glide.with(itemView.context).load(item.imageUrl).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsViewHolder {
        val binding = ArtistsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtistsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])
    }

    fun addItems(items : List<ArtistsModel>) = diffUtil.submitList(items)
}