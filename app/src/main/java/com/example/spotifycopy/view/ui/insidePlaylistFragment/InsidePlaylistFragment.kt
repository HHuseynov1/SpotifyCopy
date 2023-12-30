package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentInsidePlaylistBinding

class InsidePlaylistFragment : Fragment() {

    lateinit var binding : FragmentInsidePlaylistBinding
    private val myAdapter by lazy { InsidePlaylistAdapter() }
    private val viewModel : InsidePlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsidePlaylistBinding.inflate(inflater,container,false)

        binding.rvPlaylist.adapter = myAdapter
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItems()
    }

    private fun addItems(){
        viewModel.mutableLiveData.observe(viewLifecycleOwner){
            myAdapter.addItems(it)
        }
    }
}