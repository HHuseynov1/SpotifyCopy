package com.example.spotifycopy.view.ui.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.databinding.FragmentHomeBinding
import com.example.spotifycopy.view.ui.homeFragment.cartItem.CartItemAdapter
import com.example.spotifycopy.view.ui.homeFragment.songsForYou.SongsForYouAdapter

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    private val cartAdapter by lazy { CartItemAdapter() }
    private val songsForYouAdapter by lazy { SongsForYouAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        val mainActivity = activity as MainActivity

        viewModel.mutableLiveDataUser.observe(viewLifecycleOwner){
            for(i in it){
                Glide.with(requireContext()).load(i.imgProfile).into(binding.profileImage)
            }
        }

        binding.rvMix.adapter = cartAdapter
        binding.rvForYou.adapter = songsForYouAdapter

        binding.profileImage.setOnClickListener {
            mainActivity.openDrawer()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCart()
        addSong()
    }

    private fun addCart(){
        viewModel.mutableLiveDataCart.observe(viewLifecycleOwner){
            cartAdapter.addItems(it)
        }
    }

    private fun addSong(){
        viewModel.mutableLiveDataSong.observe(viewLifecycleOwner){
            songsForYouAdapter.addItems(it)
        }
    }

}