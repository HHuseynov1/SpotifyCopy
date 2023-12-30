package com.example.spotifycopy.view.ui.songFragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.spotifycopy.R
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.databinding.FragmentSongBinding

class SongFragment : Fragment() {

    private lateinit var binding: FragmentSongBinding

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var songList: List<Song>

    private var currentSongIndex = 0

    private val viewModel: SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()

        songList = ArrayList()

        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            songList = it
            if (songList.isNotEmpty()) {
                updateUI(currentSongIndex)
            }
        }

        binding.btnPlay.setOnClickListener {
            togglePlayback()
        }

        binding.btnNext.setOnClickListener {
            skipToNextSong()
        }

        binding.btnPrevious.setOnClickListener {
            skipToPreviousSong()
        }

    }

    private fun togglePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
        } else if (currentSongIndex == 0) {
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
            binding.btnPlay.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
                } else {
                    mediaPlayer.start()
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                }
            }
        } else {
            mediaPlayer.start()
            binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
        }
    }

    private fun skipToPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            updateUI(currentSongIndex)
        } else {
            currentSongIndex = 0
        }
    }

    private fun skipToNextSong() {
        currentSongIndex = (currentSongIndex + 1) % songList.size
        updateUI(currentSongIndex)
    }

    private fun updateUI(songIndex: Int) {
        binding.cardView.translationX = 1000f
        binding.cardView.alpha = 0f

        initializeMediaPlayer(songIndex)

        binding.cardView.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(500)
            .withEndAction {
                Glide.with(requireContext())
                    .load(songList[songIndex].imageUrl)
                    .into(binding.songImage)

                binding.txtSongName.text = songList[songIndex].title
                binding.txtArtistName.text = songList[songIndex].artist
            }
            .start()
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
            initialiseSeekbar()
            binding.btnPlay.visibility = View.VISIBLE
            togglePlayback()
            mediaPlayer.setOnErrorListener { _, what, extra ->
                println("MediaPlayer error: what=$what, extra=$extra")
                false
            }
        }
    }

    private fun initialiseSeekbar() {
        binding.seekBar.max = mediaPlayer.duration

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer.currentPosition

                    val currentDurationInMillis = mediaPlayer.currentPosition
                    val minutes = (currentDurationInMillis / 1000) / 60
                    val seconds = (currentDurationInMillis / 1000) % 60
                    val formattedCurrentDuration = String.format("%02d:%02d", minutes, seconds)

                    val durationInMillis = mediaPlayer.duration
                    val dMinutes = (durationInMillis / 1000) / 60
                    val dSeconds = (durationInMillis / 1000) % 60
                    val formattedDuration = String.format("%02d:%02d", dMinutes, dSeconds)

                    binding.txtTimeStart.text = formattedCurrentDuration
                    binding.txtTimeEnd.text = formattedDuration
                    handler.postDelayed(this, 1000)
                } catch (e: java.lang.Exception) {
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }
}