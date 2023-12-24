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
import com.bumptech.glide.Glide
import com.example.spotifycopy.R
import com.example.spotifycopy.databinding.FragmentSongBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.io.File
import java.io.IOException

class SongFragment : Fragment() {

    private lateinit var binding: FragmentSongBinding

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var progressDialog: ProgressDialog
    private var currentSongIndex = 0

    private val storage = Firebase.storage
    private val storageReference = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(requireContext())

        mediaPlayer = MediaPlayer()

        updateUI(currentSongIndex)

        binding.button.setOnClickListener {
            togglePlayback()
        }

        binding.btnskip.setOnClickListener {
            skipToNextSong()
        }

        return binding.root
    }

    private fun togglePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.button.setBackgroundResource(R.drawable.baseline_play_circle_24)
        } else {
            mediaPlayer.start()
            binding.button.setBackgroundResource(R.drawable.baseline_pause_circle_24)
        }
    }

    private fun skipToNextSong() {
        currentSongIndex = (currentSongIndex + 1) % songList.size
        updateUI(currentSongIndex)
    }

    private fun updateUI(songIndex: Int) {
        progressDialog.setMessage("Fetching data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val songReference = storageReference.child(songList[songIndex].audioPath)
        val imageReference = storageReference.child(songList[songIndex].imagePath)

        // Fetch audio file
            initializeMediaPlayer(songReference)
        
        // Fetch image file
        val localFileImage = File.createTempFile("tempFile", "png")
        imageReference.getFile(localFileImage).addOnSuccessListener {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
            // Load the image into the imageView using Glide
            Glide.with(requireContext()).load(localFileImage).into(binding.imageView)
        }.addOnFailureListener {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeMediaPlayer(songReference: StorageReference) {
            mediaPlayer.reset()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            songReference.downloadUrl.addOnSuccessListener { uri ->
                mediaPlayer.setDataSource(uri.toString())
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    binding.button.visibility = View.VISIBLE
                }
                mediaPlayer.setOnErrorListener { _, what, extra ->
                    println("MediaPlayer error: what=$what, extra=$extra")
                    false
                }
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        data class Song(val audioPath: String, val imagePath: String)

        val songList = listOf(
            Song("song-1/song_1.mp3", "song-1/1.png"),
            Song("song-2/song_2.mp3", "song-2/2.png"),
            Song("song-3/song_3.mp3", "song-3/3.png"),
            Song("song-4/song_4.mp3", "song-4/4.png"),
            Song("song-5/song_5.mp3", "song-5/5.png"),
        )
    }
}