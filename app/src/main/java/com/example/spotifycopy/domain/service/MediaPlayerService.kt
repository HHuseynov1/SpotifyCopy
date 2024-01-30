package com.example.spotifycopy.domain.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.session.PlaybackState.ACTION_PLAY_PAUSE
import android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT
import android.media.session.PlaybackState.ACTION_SKIP_TO_PREVIOUS
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.utils.CurrentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusicLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : Service() {

    private val TAG = "MusicService"
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var songs: ArrayList<SongModel>
    var songIndex: Int = 0
    private var currentMusic: String = ""
    private lateinit var mediaSession: MediaSessionCompat
    val musicIsplaying = MutableLiveData<Boolean>()

    @Inject
    lateinit var repo: Repository

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaSession = MediaSessionCompat(this, "Player Audio")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() called")

        when (intent?.action) {
            ACTION_PLAY_PAUSE.toString() -> {
                //            val songPath = intent.getStringExtra("song_path")
                if (isMusicPlaying()) {
                    // Eğer müzik çalıyorsa, duraklat
                    pauseSong()
                } else {
                    // Eğer müzik duraksı ise, en son bilinen konumdan devam et
                    playSong(songs[songIndex].songUrl)
//                    currentMusicLiveData.postValue(songs[songIndex])//
                }
            }

            ACTION_SKIP_TO_NEXT.toString() -> {
                skipToNextSong()
            }

            ACTION_SKIP_TO_PREVIOUS.toString() -> {
                skipToPreviousSong()
            }
        }
        return START_NOT_STICKY
    }

    inner class MusicPlayerBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }

    private val binder = MusicPlayerBinder()


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBind(intent: Intent): IBinder {
        songs = ArrayList()
        songIndex = intent.getIntExtra("song_index", 0)
        songs = intent.getParcelableArrayListExtra<SongModel>("musicList") as ArrayList<SongModel>

        mediaPlayer.setOnCompletionListener {
            skipToNextSong()
        }

        playSong(songs[songIndex].songUrl)
//        currentMusicLiveData.postValue(songs[songIndex])
        return binder
    }

    override fun onDestroy() {
        // Release the MediaPlayer object
        mediaPlayer.release()
    }
    // Bu özel seekTo yöntemini ekleyin

    @SuppressLint("DiscouragedApi")
    @RequiresApi(Build.VERSION_CODES.S)
    fun playSong(songPath: String?) {
//        if (songPath.isNullOrEmpty()) return
        try {
            musicIsplaying.postValue(true)
            if (mediaPlayer.currentPosition == 0 || songPath != currentMusic) {

                mediaPlayer.stop()
                mediaPlayer.reset()

                // Set the song path to the MediaPlayer object
                mediaPlayer.setDataSource(songPath)

                // Prepare the MediaPlayer object
                mediaPlayer.prepare()
                currentMusic = songPath!!
                val music = songs[songIndex]
//   /storage/emulated/0/Sounds/20230118_213636.m4a

                Log.e("songpath", "playSong: $songPath")
                val context: Context = applicationContext
//                Glide.with(this).load(music.imageUrl).into()
//                val backgroundBitmap = BitmapFactory.decodeResource(resources, resourceId)
                currentMusicLiveData.postValue(songs[songIndex])
                CurrentMusic.currentMusic.postValue(songPath)
                // Start playing the song
                //music.lastPlayTime=System.currentTimeMillis()

//                CoroutineScope(IO).launch {
//                    repo.insertMusic(music)
//                }
                mediaPlayer.start()
            } else {
                mediaPlayer.start()
            }
            // Stop any currently playing song

        } catch (e: Exception) {
            Log.e(TAG, "Error playing song: ${e.message}")
        }
    }

    fun isMusicPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

//    fun seekTo(progress: Int) {
//        mediaPlayer.seekTo(progress)
//    }
    // Bu özel seekTo yöntemini ekleyin

    fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            musicIsplaying.postValue(false)
        }
    }

    fun stopSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun skipToNextSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        songIndex = (songIndex + 1) % songs.size
        val nextSongPath = songs[songIndex].songUrl
//        currentMusicLiveData.postValue(songs[songIndex])
        CurrentMusic.currentMusic.postValue(nextSongPath)
        currentMusicLiveData.postValue(songs[songIndex])

        playSong(nextSongPath)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun skipToPreviousSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        songIndex = (songIndex - 1) % songs.size
        if (songIndex < 0) {
            songIndex = songs.size - 1
        }
        val previousSongPath = songs[songIndex].songUrl
//        currentMusicLiveData.postValue(songs[songIndex])
        CurrentMusic.currentMusic.postValue(previousSongPath)
        currentMusicLiveData.postValue(songs[songIndex])


        playSong(previousSongPath)
    }
}