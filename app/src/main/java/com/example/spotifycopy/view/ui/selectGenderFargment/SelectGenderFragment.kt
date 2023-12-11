package com.example.spotifycopy.view.ui.selectGenderFargment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifycopy.R

import com.example.spotifycopy.databinding.FragmentSelectGenderBinding
import com.example.spotifycopy.databinding.SelectGenderItemBinding

class SelectGenderFragment : Fragment() {
    private lateinit var binding: FragmentSelectGenderBinding
    private val myGenderAdapter by lazy { SelectGenderAdapter(
        onClick = {btnClick -> onClick(btnClick) }
    ) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGender.adapter = myGenderAdapter
        binding.rvGender.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, true)

        addItems()
    }

    private fun addItems() {
        val list =
            listOf(
                Genders("Prefer not to say"),
                Genders("Other"),
                Genders("Non-binary"),
                Genders("Male"),
                Genders("Female")
            )
        myGenderAdapter.addItems(list)
    }

    private fun onClick(bindingGenderItem: SelectGenderItemBinding){
        bindingGenderItem.btnGender.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }
    }
}