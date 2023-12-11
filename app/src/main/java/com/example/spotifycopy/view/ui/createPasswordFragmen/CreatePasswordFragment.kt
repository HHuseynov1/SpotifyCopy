package com.example.spotifycopy.view.ui.createPasswordFragmen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentCreatePasswordBinding

class CreatePasswordFragment : Fragment() {

    private lateinit var binding: FragmentCreatePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

    }

    private fun onClick() {
//        if(binding.edtEmail.text.toString().isNotEmpty()){
        binding.btnPassword.setOnClickListener {
            findNavController().navigate(R.id.selectGenderFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}