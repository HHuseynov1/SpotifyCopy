package com.example.spotifycopy.view.ui.createPasswordFragmen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentCreatePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnPassword.isEnabled = (s?.length ?: 0) >= 8
            }
        })

        binding.btnPassword.isEnabled = (binding.edtPassword.text?.length ?: 0) >= 8

        binding.btnPassword.setOnClickListener {
            val enteredPassword = binding.edtPassword.text.toString()

            if (enteredPassword.length >= 8) {
                findNavController().navigate(R.id.selectGenderFragment)
            } else {
                Toast.makeText(requireContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}