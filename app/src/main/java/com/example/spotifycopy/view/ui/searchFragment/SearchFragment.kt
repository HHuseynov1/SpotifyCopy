package com.example.spotifycopy.view.ui.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        val mainActivity = activity as MainActivity

        binding.profileImage.setImageResource(R.drawable.spotifylogo)

        binding.profileImage.setOnClickListener {
            mainActivity.openDrawer()
        }

        return binding.root
    }

}