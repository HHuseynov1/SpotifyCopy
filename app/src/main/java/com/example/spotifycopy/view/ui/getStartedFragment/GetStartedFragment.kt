package com.example.spotifycopy.view.ui.getStartedFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentGetStartedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetStartedFragment : Fragment() {
    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetStartedBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick(){
        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_login)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_createAccountFragment)
        }
    }
}