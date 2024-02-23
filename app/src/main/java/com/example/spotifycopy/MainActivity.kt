package com.example.spotifycopy

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
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.spotifycopy.data.other.Constants
import com.example.spotifycopy.data.other.Constants.EXTRA_MUSIC_LIST
import com.example.spotifycopy.data.other.Constants.EXTRA_SONG_INDEX
import com.example.spotifycopy.databinding.ActivityMainBinding
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.service.MediaPlayerService
import com.example.spotifycopy.utils.CurrentMusic.currentMusic
import com.example.spotifycopy.utils.CurrentMusic.currentMusicLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val viewModel by viewModels<MainViewModel>()
    private val swipeSongAdapter by lazy { SwipeSongAdapter() }
    private var curPlayingSong: SongModel? = null
    private lateinit var mediaPlayer: MediaPlayer
    private var mediaService: MediaPlayerService? = null
    private lateinit var songList: List<SongModel>
    private var isMusicPlaying: Boolean = false

    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MusicPlayerBinder
            mediaService = binder.getService()
            isMusicPlaying = binder.getService().isMusicPlaying(true)
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            mediaService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value as Boolean
            }
        }

        mediaPlayer = MediaPlayer()

        //isMusicPlaying = mediaService?.isMusicPlaying() ?: false

        binding.vpSong.adapter = swipeSongAdapter

        viewModel.mutableLiveDataSong.observe(this) {
            swipeSongAdapter.songs = it
        }

        viewModel.mutableLiveDataSong.observe(this) {
            songList = it
        }

        bottomView()
        navigationViewAccess()
        navigationView()
        viewPagerGone()

        binding.vpSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val selectedSong = swipeSongAdapter.songs.getOrNull(position)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && selectedSong != null) {
                    startService(position)
                }
            }
        })

        binding.playButton.setOnClickListener {
            togglePlayBack()
        }

        swipeSongAdapter.setOnItemClickListener(object : SwipeSongAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                openSongFragment(position)
            }
        })

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, MediaPlayerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun startService(position: Int) {
        val serviceIntent = Intent(this, MediaPlayerService::class.java).apply {
            putExtra(EXTRA_SONG_INDEX, position)
            putParcelableArrayListExtra(
                EXTRA_MUSIC_LIST,
                songList as ArrayList<out Parcelable>
            )
        }

        lifecycleScope.launch(Dispatchers.IO) {
            startService(serviceIntent)
        }
    }

    private fun openSongFragment(position : Int) {
        Log.e("positionMain",position.toString())
        val bundle = Bundle()
        bundle.putInt("Position",position)
        findNavController(R.id.fragmentContainerView).navigate(R.id.openSongFragment,bundle)
    }

    private fun togglePlayBack() {
        if (isServiceBound) {
            Log.e("isMusicPlaying", isMusicPlaying.toString())
            isMusicPlaying = if (isMusicPlaying) {
                mediaService?.pauseSong()
                binding.playButton.setBackgroundResource(R.drawable.baseline_play_arrow_24)
                false
            } else {
                currentMusic.observe(this) { currentMusic ->
                    mediaService?.playSong(currentMusic)

                    Log.e("positionSongFragment", currentMusic)
                }
                binding.playButton.setBackgroundResource(R.drawable.baseline_pause_24)
                true
            }
        }
    }

    private fun switchViewPagerToCurrentSong(song: SongModel) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (swipeSongAdapter.songs.isNotEmpty() && newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            curPlayingSong = song
        }
    }

    private fun updateUI(song: SongModel) {
        switchViewPagerToCurrentSong(song)
        Glide.with(this).load(song.imageUrl).into(binding.imgSong)
    }

    fun subscribeToObserve() {
        Log.e("subscribeToObserve", "subscribeToObserve")

        lifecycleScope.launch {
            viewPagerVisible()

            currentMusicLiveData.observe(this@MainActivity) { selectedSong ->
                Log.e("calan mahni", selectedSong.toString())
                if (selectedSong != null) {
                    updateUI(selectedSong)
                    binding.playButton.setBackgroundResource(R.drawable.baseline_pause_24)
                } else {
                    Log.e("selectedSong is null", "selectedSong nulldur")
                }
            }
        }
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun bottomView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.getStartedFragment,
                R.id.createEmailFragment,
                R.id.createPasswordFargment,
                R.id.selectGenderFragment,
                R.id.startListeningFragmentArtists,
                R.id.startListeningFragmentEnd,
                R.id.login,
                R.id.songFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.homeFragment,
                R.id.searchFragment,
                R.id.libraryFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigationView() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.whatsNew -> Toast.makeText(this, "What's New", Toast.LENGTH_LONG).show()
                R.id.listeningHistory -> Toast.makeText(
                    this,
                    "Listening History",
                    Toast.LENGTH_LONG
                ).show()

                R.id.settings -> Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
            }
            true
        }

        viewModel.mutableLiveData.observe(this) {
            val header = binding.navView.inflateHeaderView(R.layout.nav_header)
            for (i in it) {
                header.findViewById<TextView>(R.id.txtName).text = i.userName
                Glide.with(this).load(i.imgProfile).into(header.findViewById(R.id.circleImageView))
            }
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun viewPagerGone() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.getStartedFragment,
                R.id.createEmailFragment,
                R.id.createPasswordFargment,
                R.id.selectGenderFragment,
                R.id.startListeningFragmentArtists,
                R.id.startListeningFragmentEnd,
                R.id.login,
                R.id.songFragment -> {
                    binding.vpSong.visibility = View.GONE
                    binding.imgCard.visibility = View.GONE
                    binding.playButton.visibility = View.GONE
                }
            }
        }
    }

    private fun viewPagerVisible() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.libraryFragment,
                R.id.insidePlaylistFragment,
                R.id.searchInsideFragment -> {
                    binding.vpSong.visibility = View.VISIBLE
                    binding.imgCard.visibility = View.VISIBLE
                    binding.playButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigationViewAccess() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.getStartedFragment,
                R.id.createEmailFragment,
                R.id.createPasswordFargment,
                R.id.selectGenderFragment,
                R.id.startListeningFragmentArtists,
                R.id.startListeningFragmentEnd,
                R.id.songFragment,
                R.id.insidePlaylistFragment,
                R.id.login -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                R.id.homeFragment,
                R.id.searchFragment,
                R.id.libraryFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isServiceBound) {
            unbindService(serviceConnection)
        }
    }
}