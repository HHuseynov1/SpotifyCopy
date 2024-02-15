package com.example.spotifycopy.view.ui.libraryFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentLibraryBinding
import com.example.spotifycopy.view.ui.libraryFragment.artist.ArtistsItemAdapter
import com.example.spotifycopy.view.ui.libraryFragment.playlis.PlaylistItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    lateinit var binding: FragmentLibraryBinding

    private val myAdapterPlaylist by lazy {
        PlaylistItemAdapter(
            insideFragment = { insideFragment() }
        )
    }

    private val myAdapterArtist by lazy { ArtistsItemAdapter() }

    private val viewModel: LibraryPlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        binding.rvPlaylist.adapter = myAdapterPlaylist
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        binding.rvArtist.adapter = myAdapterArtist
        binding.rvArtist.layoutManager = LinearLayoutManager(requireContext())

        viewModel.mutableLiveDataUser.observe(viewLifecycleOwner) {
            for (i in it) {
                Glide.with(requireContext()).load(i.imgProfile).into(binding.profileImage)
            }
        }

        binding.chipArtists.setOnClickListener {
            binding.rvPlaylist.visibility = View.GONE
            binding.rvArtist.visibility = View.VISIBLE
            binding.chipPlaylist.visibility = View.GONE
            binding.removeSelection.visibility = View.VISIBLE
        }

        binding.chipPlaylist.setOnClickListener {
            binding.rvArtist.visibility = View.GONE
            binding.rvPlaylist.visibility = View.VISIBLE
            binding.chipArtists.visibility = View.GONE
            binding.removeSelection.visibility = View.VISIBLE
        }

        binding.removeSelection.setOnClickListener {
            binding.removeSelection.visibility = View.GONE
            binding.chipPlaylist.visibility = View.VISIBLE
            binding.chipArtists.visibility = View.VISIBLE
            binding.rvArtist.visibility = View.VISIBLE
            binding.rvPlaylist.visibility = View.VISIBLE
        }

        binding.profileImage.setOnClickListener {
            mainActivity.openDrawer()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPlaylist()
        addArtists()

    }

    private fun addPlaylist() {
        viewModel.mutableLiveDataUser.observe(viewLifecycleOwner) {
            myAdapterPlaylist.addPlaylist(it)
        }
    }

    private fun addArtists() {
        viewModel.mutableLiveDataArtists.observe(viewLifecycleOwner) {
            myAdapterArtist.addArtists(it)
        }
    }

    private fun insideFragment() {
        findNavController().navigate(R.id.insidePlaylistFragment)
    }

}