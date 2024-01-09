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
import com.example.spotifycopy.databinding.FragmentSearchBinding
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    lateinit var binding: FragmentLibraryBinding

    private val myAdapter by lazy { PlaylistItemAdapter(
       insideFragment = { insideFragment() }
    ) }

    private val viewModel : LibraryPlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater,container,false)

        val mainActivity = activity as MainActivity

        binding.rvPlaylist.adapter = myAdapter
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        viewModel.mutableLiveData.observe(viewLifecycleOwner){
            for(i in it){
                Glide.with(requireContext()).load(i.imgProfile).into(binding.profileImage)
            }
        }

        binding.profileImage.setOnClickListener {
            mainActivity.openDrawer()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPlaylist()
    }

    private fun addPlaylist(){
        viewModel.mutableLiveData.observe(viewLifecycleOwner){
            myAdapter.addPlaylist(it)
        }
    }

    private fun insideFragment(){
        findNavController().navigate(R.id.insidePlaylistFragment)
    }

}