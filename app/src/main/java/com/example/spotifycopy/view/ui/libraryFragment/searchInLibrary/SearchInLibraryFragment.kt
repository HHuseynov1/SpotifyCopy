package com.example.spotifycopy.view.ui.libraryFragment.searchInLibrary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSearchInLibraryBinding
import com.example.spotifycopy.domain.models.LibraryModel
import com.example.spotifycopy.domain.models.SongModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchInLibraryFragment : Fragment() {

    lateinit var binding: FragmentSearchInLibraryBinding

    private val viewModel: SearchInLibraryViewModel by viewModels()

    private val myAdapter by lazy { SearchInLibraryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchInLibraryBinding.inflate(inflater, container, false)

        binding.rvLibrary.adapter = myAdapter
        binding.rvLibrary.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        searchData()
    }

    private fun searchData() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.rvLibrary.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtSearch.text.isNotEmpty()) {
                    binding.rvLibrary.visibility = View.VISIBLE
                    filter(s.toString())
                    binding.texts.visibility = View.GONE

                } else {
                    binding.rvLibrary.visibility = View.GONE
                    binding.texts.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun filter(text: String) {
        val list = ArrayList<LibraryModel>()
        viewModel.mutableLiveDataLibrary.observe(viewLifecycleOwner) {
            for (item in it) {
                if (item.names.lowercase().contains(text.lowercase())) {
                    list.add(item)
                }
            }
            myAdapter.addItems(list)
        }
    }
}