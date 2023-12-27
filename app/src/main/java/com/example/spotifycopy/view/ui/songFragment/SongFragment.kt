package com.example.spotifycopy.view.ui.songFragment

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.spotifycopy.R
import com.example.spotifycopy.data.entites.Song
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.databinding.FragmentSongBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment() {

    private lateinit var binding: FragmentSongBinding

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var songList: List<Song>

    private var currentSongIndex = 0

    private val viewModel: SongViewModel by viewModels()

//    @Inject
//    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        mediaPlayer = MediaPlayer()

        songList = ArrayList()

        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            songList = it
            if(songList.isNotEmpty()){
                updateUI(currentSongIndex)
            }
        }

        binding.btnPlay.setOnClickListener {
            togglePlayback()
        }

        binding.btnNext.setOnClickListener {
            skipToNextSong()
        }

        return binding.root
    }

    private fun togglePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
        }
        else if(currentSongIndex == 0){
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
            binding.btnPlay.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
                }else {
                    mediaPlayer.start()
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                }
            }
        }
        else {
            mediaPlayer.start()
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
        }
    }

    private fun skipToNextSong() {
        currentSongIndex = (currentSongIndex + 1) % songList.size
        updateUI(currentSongIndex)
    }

    private fun updateUI(songIndex: Int) {

        initializeMediaPlayer(songIndex)

        Glide.with(requireContext()).load(songList[songIndex].imageUrl).into(binding.songImage)
    }

    private fun initializeMediaPlayer(songIndex: Int) {
        mediaPlayer.reset()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        mediaPlayer.setDataSource(songList[songIndex].songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.btnPlay.visibility = View.VISIBLE
            togglePlayback()

            mediaPlayer.setOnErrorListener { _, what, extra ->
                println("MediaPlayer error: what=$what, extra=$extra")
                false
            }
        }
    }
}