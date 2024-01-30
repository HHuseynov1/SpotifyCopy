package com.example.spotifycopy.view.ui.songFragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Parcelable
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

    private var position: Int = 0

    private var musicService: MediaPlayerService? = null
    private var isMusicServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MusicPlayerBinder
            musicService = binder.getService()
            isMusicServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isMusicServiceBound = false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            songList = it
        }

        val selectedPosition = Intent().getIntExtra("selectedPosition", 0)

        position = selectedPosition

        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        intent.putExtra("song_index", position)
        intent.putParcelableArrayListExtra(
            "musicList",
            songList as java.util.ArrayList<out Parcelable>
        )

        binding.txtSongName.text = songList[position].title
        binding.txtArtistName.text = songList[position].artist

        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimation()

        binding.txtSongName.requestFocus()
        binding.txtArtistName.requestFocus()

        Handler(Looper.getMainLooper()).postDelayed({
            // Eğer müzik duraksı ise, en son bilinen konumdan devam et
            if (CurrentMusic.currentMusic.value != songList[position].songUrl) {
                musicService?.songIndex = position
                musicService?.playSong(songList[position].songUrl)
            }
            musicService?.musicIsplaying?.observe(viewLifecycleOwner) {
                if (it) {
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                    startAnimation()
//                    binding.play.setImageResource(R.drawable.pause)
                } else {
                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
                    binding.songImage.clearAnimation()
//                    binding.play.setImageResource(R.drawable.play)

                }
            }
            initialiseSeekbar()

        }, 500)


        currentMusicLiveData.observe(viewLifecycleOwner) {
            val music = it
            binding.txtSongName.text = music.title
            binding.txtArtistName.text = music.artist

            Glide.with(requireContext()).load(music.imageUrl).into(binding.songImage)
        }

        binding.btnPlay.setOnClickListener {

            if (isMusicServiceBound && musicService != null) {

                if (musicService?.isMusicPlaying() == true) {
                    // Eğer müzik çalıyorsa, duraklat
                    musicService?.pauseSong()
                    binding.songImage.clearAnimation()
                } else {
                    startAnimation()
//                    initialiseSeekbar()
                    // Eğer müzik duraksı ise, en son bilinen konumdan devam et
                    musicService?.playSong(songList[position].songUrl)

                }
            }
        }

        binding.btnNext.setOnClickListener {
            musicService?.skipToNextSong()

        }
        binding.btnPrevious.setOnClickListener {
            musicService?.skipToPreviousSong()

        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService?.mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


//        binding.btnPlay.setOnClickListener {
//            togglePlayback()
//        }
//
//        binding.btnNext.setOnClickListener {
//            mediaPlayerService.skipToNextSong()
//        }
//
//        binding.btnPrevious.setOnClickListener {
//           mediaPlayerService.skipToPreviousSong()
//        }

    }

    private fun startAnimation() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            duration = 3000
        }
        binding.songImage.startAnimation(rotateAnimation)
    }

//    private fun togglePlayback() {
//        if (mediaPlayer.isPlaying) {
//            mediaPlayer.pause()
//            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
//        } else if (currentSongIndex == 0) {
//            binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
//            binding.btnPlay.setOnClickListener {
//                if (mediaPlayer.isPlaying) {
//                    mediaPlayer.pause()
//                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24)
//                } else {
//                    mediaPlayer.start()
//                    binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
//                }
//            }
//        } else {
//            mediaPlayer.start()
//            binding.btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24)
//        }
//    }

//    private fun skipToPreviousSong() {
//        if (currentSongIndex > 0) {
//            currentSongIndex--
//            updateUI(currentSongIndex)
//        } else {
//            currentSongIndex = 0
//        }
//    }
//
//    private fun skipToNextSong() {
//        currentSongIndex = (currentSongIndex + 1) % songList.size
//        updateUI(currentSongIndex)
//    }

//    private fun updateUI(songIndex: Int) {
//        binding.cardView.translationX = 1000f
//        binding.cardView.alpha = 0f
//
//        initializeMediaPlayer(songIndex)
//
//        binding.cardView.animate()
//            .translationX(0f)
//            .alpha(1f)
//            .setDuration(500)
//            .withEndAction {
//                Glide.with(requireContext())
//                    .load(songList[songIndex].imageUrl)
//                    .into(binding.songImage)
//
//                binding.txtSongName.text = songList[songIndex].title
//                binding.txtArtistName.text = songList[songIndex].artist
//            }
//            .start()
//    }

//    private fun initializeMediaPlayer(songIndex: Int) {
//        mediaPlayer.reset()
//        mediaPlayer.setAudioAttributes(
//            AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .build()
//        )
//
//
//        mediaPlayer.setDataSource(songList[songIndex].songUrl)
//        mediaPlayer.prepareAsync()
//        mediaPlayer.setOnPreparedListener {
//            initialiseSeekbar()
//            binding.btnPlay.visibility = View.VISIBLE
//            togglePlayback()
//            mediaPlayer.setOnErrorListener { _, what, extra ->
//                println("MediaPlayer error: what=$what, extra=$extra")
//                false
//            }
//        }
//    }

    private fun initialiseSeekbar() {
        val mp = musicService?.mediaPlayer
        binding.seekBar.max = musicService?.mediaPlayer?.duration ?: 23000

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mp!!.currentPosition

                    val currentdurationInMillis = mp.currentPosition
                    val minutes = (currentdurationInMillis / 1000) / 60
                    val seconds = (currentdurationInMillis / 1000) % 60
                    val formattedCurrentDuration = String.format("%02d:%02d", minutes, seconds)

                    val durationInMillis = mp.duration
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