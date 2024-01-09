package com.example.spotifycopy.view.ui.startListeningFragmentEnd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentStartListeningEndBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartListeningFragmentEnd : Fragment() {

    lateinit var binding : FragmentStartListeningEndBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartListeningEndBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartListening.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }

    }



}