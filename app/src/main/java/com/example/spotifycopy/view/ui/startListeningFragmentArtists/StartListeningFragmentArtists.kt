package com.example.spotifycopy.view.ui.startListeningFragmentArtists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentStartListeningArtistsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartListeningFragmentArtists : Fragment() {
    lateinit var binding: FragmentStartListeningArtistsBinding

    private val viewModel : ArtistsViewModel by viewModels()

    private val myAdapter by lazy { ArtistsItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartListeningArtistsBinding.inflate(inflater,container,false)

        binding.rvArtists.adapter = myAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addItems()
    }

    private fun addItems(){
        viewModel.artists.observe(viewLifecycleOwner){
            myAdapter.addItems(it)
        }
    }
}