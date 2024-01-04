package com.example.spotifycopy.view.ui.searchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.R
import com.example.spotifycopy.data.entities.Categories
import com.example.spotifycopy.databinding.FragmentSearchBinding
import com.example.spotifycopy.presentation.models.CategoryModel

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    private val myAdapter by lazy { CategoriesAdapter() }

    private val viewModel : SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        val mainActivity = activity as MainActivity

        binding.profileImage.setOnClickListener {
            mainActivity.openDrawer()
        }

        binding.rvCategories.adapter = myAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setImageResource(R.drawable.spotifylogo)

        addItems()

        binding.edtSearch.setOnClickListener {
            findNavController().navigate(R.id.searchInsideFragment)
        }

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.searchInsideFragment)
        }

        binding.btnText.setOnClickListener {
            findNavController().navigate(R.id.searchInsideFragment)
        }
    }

    private fun addItems(){
        viewModel.mutableLiveData.observe(viewLifecycleOwner){
                myAdapter.setImages(it)
        }
    }

}