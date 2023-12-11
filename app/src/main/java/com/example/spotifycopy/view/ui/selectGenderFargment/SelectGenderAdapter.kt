package com.example.spotifycopy.view.ui.selectGenderFargment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifycopy.databinding.SelectGenderItemBinding

class SelectGenderAdapter (private val onClick : (btnClick : SelectGenderItemBinding) -> Unit) : RecyclerView.Adapter<SelectGenderAdapter.GenderViewHolder>() {


    private val genderCallBack = object : DiffUtil.ItemCallback<Genders>() {
        override fun areItemsTheSame(oldItem: Genders, newItem: Genders): Boolean {
            return oldItem.gender == newItem.gender
        }

        override fun areContentsTheSame(oldItem: Genders, newItem: Genders): Boolean {
            return oldItem == newItem
        }
    }

    private val diffUtil = AsyncListDiffer(this, genderCallBack)

    inner class GenderViewHolder(private val binding : SelectGenderItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Genders){
            binding.btnGender.text = item.gender

            onClick(binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        val binding = SelectGenderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GenderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position])

    }

    fun addItems(item : List<Genders>){
        return diffUtil.submitList(item)
    }
}