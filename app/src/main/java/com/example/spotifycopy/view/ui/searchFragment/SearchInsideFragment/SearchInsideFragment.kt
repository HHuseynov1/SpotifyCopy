package com.example.spotifycopy.view.ui.searchFragment.SearchInsideFragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSearchInsideBinding
import com.example.spotifycopy.domain.models.SongModel
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.S)
@AndroidEntryPoint
class SearchInsideFragment : Fragment() {

    lateinit var binding: FragmentSearchInsideBinding

    private val myAdapter by lazy {
        SearchInsideAdapter(
            currentSong = { id ->
                openSong(id)
            }
        )
    }

    private val viewModel: SearchInsideViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchInsideBinding.inflate(inflater, container, false)

        binding.rvSongs.adapter = myAdapter
        binding.rvSongs.layoutManager = LinearLayoutManager(requireContext())

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
                binding.rvSongs.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtSearch.text.isNotEmpty()) {
                    binding.rvSongs.visibility = View.VISIBLE
                    filter(s.toString())
                    binding.texts.visibility = View.GONE

                } else {
                    binding.rvSongs.visibility = View.GONE
                    binding.texts.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun filter(text: String) {
        val list = ArrayList<SongModel>()
        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            for (item in it) {
                if (item.title.lowercase().contains(text.lowercase())) {
                    list.add(item)
                }
            }
            myAdapter.addItems(list)
        }
    }

    private fun openSong(position: Int) {
        val mainActivity = activity as MainActivity
        mainActivity.startService(position)
        mainActivity.subscribeToObserve()
    }
}