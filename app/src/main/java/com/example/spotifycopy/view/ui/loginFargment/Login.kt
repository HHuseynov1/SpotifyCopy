package com.example.spotifycopy.view.ui.loginFargment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : Fragment() {
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        btnClick()

        return binding.root
    }

    private fun btnClick(){
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}