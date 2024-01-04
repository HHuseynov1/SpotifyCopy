package com.example.spotifycopy.view.ui.searchFragment.SearchInsideFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSearchInsideBinding

class SearchInsideFragment : Fragment() {

    lateinit var binding : FragmentSearchInsideBinding

    private val myAdapter by lazy { SearchInsideAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchInsideBinding.inflate(inflater,container,false)

        binding.rvSongs.adapter = myAdapter
        binding.rvSongs.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.edtSearch.setOnClickListener {
         binding.rvSongs.visibility = View.VISIBLE
        }
    }
}