package com.example.spotifycopy.view.ui.songFragment

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

    private var initialTouchX: Float = 0f
    private var cardViewOriginalX: Float = 0f

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

        return binding.root
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
        val translationX = 1000f

        // Animate the translation of the CardView
        binding.cardView.translationX = translationX
        binding.cardView.alpha = 0f

        binding.seekBar.progress = mediaPlayer.currentPosition

        binding.cardView.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(500) // Adjust the duration as needed
            .withEndAction {
                // Load the new image with Glide
                Glide.with(requireContext())
                    .load(songList[songIndex].imageUrl)
                    .into(binding.songImage)

                binding.txtSongName.text = songList[songIndex].title
                binding.txtArtistName.text = songList[songIndex].artist

                // Initialize media player if needed
                initializeMediaPlayer(songIndex)
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
            binding.btnPlay.visibility = View.VISIBLE
            togglePlayback()

            mediaPlayer.setOnErrorListener { _, what, extra ->
                println("MediaPlayer error: what=$what, extra=$extra")
                false
            }
        }
    }
}