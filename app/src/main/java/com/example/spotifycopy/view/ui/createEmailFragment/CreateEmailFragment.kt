package com.example.spotifycopy.view.ui.createEmailFragment

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
import com.example.spotifycopy.databinding.FragmentCreateEmailBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEmailFragment : Fragment() {
    private lateinit var binding: FragmentCreateEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

    }

    private fun onClick() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnEmail.isEnabled = !s.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(s).matches()
            }
        })

        binding.btnEmail.isEnabled = !binding.edtEmail.text.isNullOrBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text).matches()

        binding.btnEmail.setOnClickListener {
            val enteredEmail = binding.edtEmail.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                findNavController().navigate(R.id.createPasswordFargment)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}