package com.example.spotifycopy.view.ui.songFragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.spotifycopy.R
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.databinding.FragmentSongBinding
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.service.MediaPlayerService
import com.example.spotifycopy.utils.CurrentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusicLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongFragment : Fragment() {
    private lateinit var binding: FragmentSongBinding
    private lateinit var songList: List<SongModel>
    private val viewModel: SongViewModel by viewModels()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaService: MediaPlayerService
    private var isMusicServiceBound = false
    private var musicIsPlayingSongFragment = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MusicPlayerBinder
            mediaService = binder.getService()
            musicIsPlayingSongFragment = mediaService.isMusicPlaying(true)
            isMusicServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isMusicServiceBound = false
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        mediaPlayer = MediaPlayer()

        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            songList = it
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireContext().bindService(
            Intent(requireContext(), MediaPlayerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed({
            currentMusicLiveData.observe(viewLifecycleOwner) { currentSong ->
                if (currentMusic.value != currentSong.songUrl) {
                    mediaService.playSong(currentSong.songUrl)
                }
                if (musicIsPlayingSongFragment) {
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                } else {
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
                }
                initialiseSeekbar()
            }
        }, 500)

        currentSong()

        binding.btnPlay.setOnClickListener {
            togglePlayBack()
        }

        binding.btnNext.setOnClickListener {
            mediaService.skipToNextSong()
        }
        binding.btnPrevious.setOnClickListener {
            mediaService.skipToPreviousSong()

        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun currentSong() {
        currentMusicLiveData.observe(viewLifecycleOwner) {
            Log.e("currentMusic", it.toString())
            binding.txtSongName.text = it.title
            binding.txtArtistName.text = it.artist
            Glide.with(requireContext()).load(it.imageUrl).into(binding.songImage)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun togglePlayBack() {
        if (isMusicServiceBound) {
            musicIsPlayingSongFragment = if (musicIsPlayingSongFragment) {
                Log.e("musicIsPlaying", musicIsPlayingSongFragment.toString())
                mediaService.pauseSong()
                binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
                Log.e("musicIsPlaying", musicIsPlayingSongFragment.toString())
                false
            } else {
                currentMusic.observe(viewLifecycleOwner) { currentMusic ->
                    mediaService.playSong(currentMusic)
                    Log.e("positionSongFragment", currentMusic)
                }
                binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                Log.e("musicIsPlaying", musicIsPlayingSongFragment.toString())
                true
            }
        }
    }

    private fun initialiseSeekbar() {
        val mp = mediaService.mediaPlayer
        binding.seekBar.max = mediaService.mediaPlayer.duration

        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mp.currentPosition

                    val currentdurationInMillis = mp.currentPosition
                    val minutes = (currentdurationInMillis / 1000) / 60
                    val seconds = (currentdurationInMillis / 1000) % 60
                    val formattedCurrentDuration = String.format("%02d:%02d", minutes, seconds)

                    val durationInMillis = mp.duration
                    val dminutes = (durationInMillis / 1000) / 60
                    val dseconds = (durationInMillis / 1000) % 60
                    val formattedDuration = String.format("%02d:%02d", dminutes, dseconds)

                    binding.txtTimeStart.text = formattedCurrentDuration
                    binding.txtTimeEnd.text = formattedDuration
                    handler.postDelayed(this, 1000)
                } catch (e: java.lang.Exception) {
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }

    override fun onStop() {
        super.onStop()
        if (isMusicServiceBound) {
            requireContext().unbindService(serviceConnection)
            isMusicServiceBound = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

}
