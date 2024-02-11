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

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MusicPlayerBinder
            mediaService = binder.getService()
            isMusicServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isMusicServiceBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        mediaPlayer = MediaPlayer()

        viewModel.mutableLiveData.observe(viewLifecycleOwner){
            songList = it
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireContext().bindService(Intent(requireContext(), MediaPlayerService::class.java),serviceConnection,Context.BIND_AUTO_CREATE)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Handler(Looper.getMainLooper()).postDelayed({
//            // Eğer müzik duraksı ise, en son bilinen konumdan devam et
//            if(CurrentMusic.currentMusic.value!=songList[position].songUrl){
//                mediaService.songIndex = position
//                mediaService.playSong(songList[position].songUrl)
//            }
//            mediaService.musicIsPlaying.observe(viewLifecycleOwner){
//                if(it){
//                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
////                    binding.play.setImageResource(R.drawable.pause)
//                }else{
//                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
//                    binding.songImage.clearAnimation()
////                    binding.play.setImageResource(R.drawable.play)
//
//                }
//            }
//            initialiseSeekbar()
//
//        }, 500)

        currentMusicLiveData.observe(viewLifecycleOwner) {
            Log.e("currenMusic", it.toString())
            val music = it
            binding.txtSongName.text = music.title
            binding.txtArtistName.text = music.artist
            Glide.with(requireContext()).load(music.imageUrl).into(binding.songImage)
            mediaService.playSong(it.songUrl)
        }

//        binding.btnPlay.setOnClickListener {
//            if (isMusicServiceBound) {
//                if (mediaService.isMusicPlaying()) {
//                    // Eğer müzik çalıyorsa, duraklat
//                    mediaService.pauseSong()
//                } else {
//                    initialiseSeekbar()
//                    // Eğer müzik duraksı ise, en son bilinen konumdan devam et
//                    mediaService.playSong(songList[position].songUrl)
//                    Log.e("positionSongFragment",position.toString())
//                }
//            }
//        }

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


    private fun initialiseSeekbar() {
        binding.seekBar.max = mediaPlayer.duration

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer.currentPosition

                    val currentdurationInMillis = mediaPlayer.currentPosition
                    val minutes = (currentdurationInMillis / 1000) / 60
                    val seconds = (currentdurationInMillis / 1000) % 60
                    val formattedCurrentDuration = String.format("%02d:%02d", minutes, seconds)

                    val durationInMillis = mediaPlayer.duration
                    val dminutes = (durationInMillis / 1000) / 60
                    val dseconds = (durationInMillis / 1000) % 60
                    val formattedDuration = String.format("%02d:%02d", dminutes, dseconds)

                    // TextView içinde süreyi gösterin
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