package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentInsidePlaylistBinding
import com.example.spotifycopy.domain.models.UserModel

class InsidePlaylistFragment : Fragment() {

    lateinit var binding: FragmentInsidePlaylistBinding
    private val myAdapter by lazy { InsidePlaylistAdapter(
        currentSong = {position -> onItemClick(position)}
    ) }
    private val viewModel: InsidePlaylistViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsidePlaylistBinding.inflate(inflater, container, false)

        binding.rvPlaylist.adapter = myAdapter
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItems()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        playlist()
    }

    private fun addItems() {
        viewModel.mutableLiveDataSong.observe(viewLifecycleOwner) {
            myAdapter.addItems(it)
        }
    }

    private fun playlist() {
        viewModel.mutableLiveDataUser.observe(viewLifecycleOwner) {
            for (item in it) {
                Glide.with(requireContext()).load(item.imgPlaylist).into(binding.imgPlaylist)
                binding.txtPlaylistName.text = item.playlistName
                binding.txtUserName.text = item.userName
                Glide.with(requireContext()).load(item.imgProfile).into(binding.profileImage)
            }
        }
    }

    private fun onItemClick(position : Int){
        val action = InsidePlaylistFragmentDirections.actionInsidePlaylistFragmentToSongFragment(position)
        findNavController().navigate(action)
    }
}