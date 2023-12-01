package com.example.spotifycopy.ui.createEmailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentCreateEmailBinding

class CreateEmailFragment : Fragment() {

    private lateinit var binding: FragmentCreateEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

    }

    private fun onClick() {
//        if(binding.edtEmail.text.toString().isNotEmpty()){
        binding.btnEmail.setOnClickListener {
            findNavController().navigate(R.id.createPasswordFargment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}