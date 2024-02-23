package com.example.spotifycopy.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.spotifycopy.MainActivity
import com.example.spotifycopy.R
import com.example.spotifycopy.data.other.Constants.ACTION_PLAY_PAUSE
import com.example.spotifycopy.data.other.Constants.ACTION_SKIP_TO_NEXT
import com.example.spotifycopy.data.other.Constants.ACTION_SKIP_TO_PREVIOUS
import com.example.spotifycopy.data.other.Constants.EXTRA_MUSIC_LIST
import com.example.spotifycopy.data.other.Constants.EXTRA_SONG_INDEX
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.utils.CurrentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusicLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : Service() {

    private val TAG = "MediaPlayerService"
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var songs: ArrayList<SongModel>
    var songIndex: Int = 0
    private var currentMusic: String = ""
    private lateinit var mediaSession: MediaSessionCompat

    private val NOTIFICATION_CHANNEL_ID = "MusicPlayerChannelId"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaSession = MediaSessionCompat(this, "Player Audio")
        songs = ArrayList()
        createNotificationChannel()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() called")

        intent?.let {

            onBind(it)

            GlobalScope.launch {
                withContext(Dispatchers.Default) {
                    if (songIndex in songs.indices) {
                        if (songs.isNotEmpty()) {
                            when (it.action) {
                                ACTION_PLAY_PAUSE -> {
                                    if (mediaPlayer.isPlaying) {
                                        pauseSong()
                                    } else {
                                        playSong(songs[songIndex].songUrl)

                                        Log.e("geldi", "geldi")
                                    }
                                }

                                ACTION_SKIP_TO_NEXT -> {
                                    skipToNextSong()
                                }

                                ACTION_SKIP_TO_PREVIOUS -> {
                                    skipToPreviousSong()
                                }

                                else -> {
                                    Log.e(TAG, "songs is empty")
                                }
                            }
                        } else {
                            // Handle the case where songs is empty
                            Log.e(TAG, "songs is empty!")
                        }
                    } else {
                        Log.e(TAG, "Invalid initial songIndex provided!")
                        // Handle the case where the initial songIndex is not valid
                    }
                }
            }
        }

        return START_NOT_STICKY
    }

    inner class MusicPlayerBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }

    private val binder = MusicPlayerBinder()

    override fun onBind(intent: Intent): IBinder {
        val incomingSongList = intent.getParcelableArrayListExtra<SongModel>(EXTRA_MUSIC_LIST)
        val incomingSongIndex = intent.getIntExtra(EXTRA_SONG_INDEX, 0)

        if (incomingSongList.isNullOrEmpty()) {
            Log.e(TAG, "Song list is null or empty")
            // Handle the case where songs is null or empty
            return binder
        }

        if (incomingSongIndex < 0 || incomingSongIndex >= incomingSongList.size) {
            Log.e(TAG, "Invalid song index provided")
            return binder
        }

        songs = ArrayList(incomingSongList)
        songIndex = incomingSongIndex

        Log.e("serviceSongIndex", songIndex.toString())
        Log.e("serviceSongList", songs.toString())

        mediaPlayer.setOnCompletionListener {
            skipToNextSong()
        }

        if (songs.isNotEmpty()) {
            playSong(songs[songIndex].songUrl)
            Log.e("bind-a geldi", "bind-a geldi")
        } else {
            Log.e(TAG, "Songs is empty!")
        }

        return binder
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun playSong(songPath: String?) {
        try {
            isMusicPlaying(true)
            if (mediaPlayer.currentPosition == 0 || songPath != currentMusic) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songPath)
                mediaPlayer.prepare()
                currentMusic = songPath!!
                val music = songs[songIndex]
                currentMusicLiveData.postValue(songs[songIndex])
                Log.e("currentMusicLiveData", currentMusicLiveData.toString())
                CurrentMusic.currentMusic.postValue(songPath)
                showNotification(music.title, music.artist, null)
                Log.e("music", music.toString())
                mediaPlayer.start()
            } else {
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing song: ${e.message}")
        }
    }

    fun isMusicPlaying(check: Boolean): Boolean {
//        Log.e("isPlaying", mediaPlayer.isPlaying.toString())
//        var isPlaying = false
//        if (songIndex >= 0) {
//            isPlaying = true
//        }
//        Log.e("isPlaying", isPlaying.toString())
        return check
    }

    fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isMusicPlaying(false)
        }
    }

    fun stopSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    fun skipToNextSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        songIndex = (songIndex + 1) % songs.size
        val nextSongPath = songs[songIndex].songUrl
        CurrentMusic.currentMusic.postValue(nextSongPath)
        currentMusicLiveData.postValue(songs[songIndex])
        playSong(nextSongPath)
    }

    fun skipToPreviousSong() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        songIndex = (songIndex - 1 + songs.size) % songs.size
        val previousSongPath = songs[songIndex].songUrl
        CurrentMusic.currentMusic.postValue(previousSongPath)
        currentMusicLiveData.postValue(songs[songIndex])
        playSong(previousSongPath)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Music Player"
            val channelDescription = "Music Player Channel"

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = channelDescription
                enableLights(true)
                lightColor = Color.BLUE
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun showNotification(
        music_name: String,
        artist_name: String,
        backgroundBitmap: Bitmap?
    ) {
        Log.e(TAG, "showNotification")
        // Bildirim tıklandığında açılacak aktiviteyi ayarlayalım
        val pendingIntent = Intent(this, MainActivity::class.java).let { intent ->
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(backgroundBitmap)
            .bigLargeIcon(backgroundBitmap)

        val playPauseIntent = Intent(this, MediaPlayerService::class.java).apply {
            action = ACTION_PLAY_PAUSE

        }
        val previousIntent = Intent(this, MediaPlayerService::class.java).apply {
            action = ACTION_SKIP_TO_PREVIOUS

        }
        val nextIntent = Intent(this, MediaPlayerService::class.java).apply {
            action = ACTION_SKIP_TO_NEXT

        }

        val playPausePendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val previousPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            previousIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val nextPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(music_name)
            .setContentText(artist_name)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.drawable.baseline_skip_previous_24, "Previous", previousPendingIntent)
            .addAction(R.drawable.baseline_play_arrow_24, "Play/Pause", playPausePendingIntent)
            .addAction(R.drawable.baseline_skip_next_24, "Next", nextPendingIntent)
            .setStyle(bigPictureStyle)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    inner class MusicPlayerBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null || context == null) return

            when (intent.action) {
                ACTION_PLAY_PAUSE -> {
                    // Play/Pause eylemi burada işlenecek
                    val serviceIntent = Intent(context, MediaPlayerService::class.java)
                    serviceIntent.action = intent.action
                    context.startService(serviceIntent)
                }

                ACTION_SKIP_TO_PREVIOUS -> {
                    // Previous eylemi burada işlenecek
                    val serviceIntent = Intent(context, MediaPlayerService::class.java)
                    serviceIntent.action = intent.action
                    context.startService(serviceIntent)
                }

                ACTION_SKIP_TO_NEXT -> {
                    // Next eylemi burada işlenecek
                    val serviceIntent = Intent(context, MediaPlayerService::class.java)
                    serviceIntent.action = intent.action
                    context.startService(serviceIntent)
                }
            }
        }
    }
}