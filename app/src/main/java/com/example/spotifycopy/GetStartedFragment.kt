package com.example.spotifycopy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.databinding.FragmentGetStartedBinding

class GetStartedFragment : Fragment() {
    lateinit var binding: FragmentGetStartedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetStartedBinding.inflate(inflater,container,false)

        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_login)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_createAccountFragment)
        }

        return binding.root
    }


}