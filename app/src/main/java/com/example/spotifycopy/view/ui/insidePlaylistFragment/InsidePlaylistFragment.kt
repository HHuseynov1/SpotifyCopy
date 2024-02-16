package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.data.other.Constants.ACTION_PLAY_PAUSE
import com.example.spotifycopy.data.other.Constants.EXTRA_MUSIC_LIST
import com.example.spotifycopy.data.other.Constants.EXTRA_SONG_INDEX
import com.example.spotifycopy.databinding.FragmentInsidePlaylistBinding
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.service.MediaPlayerService
import com.example.spotifycopy.utils.CurrentMusic.currentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusicLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.S)
@AndroidEntryPoint
class InsidePlaylistFragment : Fragment() {

    lateinit var mediaPlayer: MediaPlayer

    private lateinit var listSong: List<SongModel>

    lateinit var binding: FragmentInsidePlaylistBinding
    private val myAdapter by lazy {
        InsidePlaylistAdapter(
            currentSong = { position -> onItemClick(position) }
        )
    }
    private val viewModel: InsidePlaylistViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsidePlaylistBinding.inflate(inflater, container, false)

        binding.rvPlaylist.adapter = myAdapter
        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        viewModel.mutableLiveDataSong.observe(viewLifecycleOwner) {
            listSong = it
        }

        mediaPlayer = MediaPlayer()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItems()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        playlist()
    }

    private fun addItems() {
        viewModel.mutableLiveDataSong.observe(viewLifecycleOwner) {
            myAdapter.addItems(it)
        }
    }

    private fun playlist() {
        viewModel.mutableLiveDataUser.observe(viewLifecycleOwner) {
            for (item in it) {
                Glide.with(requireContext()).load(item.imgPlaylist).into(binding.imgPlaylist)
                binding.txtPlaylistName.text = item.playlistName
                binding.txtUserName.text = item.userName
                Glide.with(requireContext()).load(item.imgProfile).into(binding.profileImage)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onItemClick(position: Int) {
        val activity = activity as MainActivity
        // startMusicPlayback(position)
        activity.startService(position)
        activity.subscribeToObserve()

    }

//    private fun startMusicPlayback(position: Int) {
//        val activity = activity as MainActivity
//        Log.e("ListSong", listSong.toString())
//        Log.e("position", position.toString())
//
//        val serviceIntent = Intent(requireContext(), MediaPlayerService::class.java).apply {
//            // action = ACTION_PLAY_PAUSE
//            putExtra(EXTRA_SONG_INDEX, position)
//            putParcelableArrayListExtra(EXTRA_MUSIC_LIST, listSong as ArrayList<out Parcelable>)
//        }
//      //  requireContext().startService(serviceIntent)
//        requireContext().bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE)
//    }
}