package com.example.spotifycopy.view.ui.selectGenderFargment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSelectGenderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectGenderFragment : Fragment() {
    private lateinit var binding: FragmentSelectGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        selectGender()
    }

    private fun selectGender(){
        binding.btnFemale.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }

        binding.btnMale.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }

        binding.btnNon.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }

        binding.btnOther.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }

        binding.btnPrefer.setOnClickListener {
            findNavController().navigate(R.id.startListeningFragmentArtists)
        }

    }

}