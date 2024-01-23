package com.example.spotifycopy

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.ActivityMainBinding
import com.example.spotifycopy.domain.models.SongModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    private val viewModel by viewModels<MainViewModel>()

    private val swipeSongAdapter by lazy { SwipeSongAdapter() }

    private var curPlayingSong: SongModel? = null

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value as Boolean
            }
        }

        mediaPlayer = MediaPlayer()

        binding.vpSong.adapter = swipeSongAdapter

        bottomView()
        navigationViewAccess()
        navigationView()
        viewPagerGone()

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

    private fun openSongFragment(position: Int) {
        val bundle = Bundle()
        bundle.putInt("selectedPosition", position)
        findNavController(R.id.fragmentContainerView).navigate(R.id.openSongFragment, bundle)
    }

    private fun togglePlayBack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            binding.playButton.setBackgroundResource(R.drawable.baseline_play_arrow_24)
        } else {
            mediaPlayer.start()
            binding.playButton.setBackgroundResource(R.drawable.baseline_pause_24)
        }
    }

    private fun switchViewPagerToCurrentSong(song: SongModel) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            curPlayingSong = song
        }
    }

    fun subscribeToObservers(position: Int) {
        viewModel.mutableLiveDataSong.observe(this) { songs ->
            swipeSongAdapter.songs = songs
            if (songs.isNotEmpty()) {
                Glide.with(this).load((curPlayingSong ?: songs[0]).imageUrl)
                    .into(binding.imgSong)
            }
        }

        viewModel.mutableLiveDataSong.observe(this) {
            if (it == null) return@observe

            Glide.with(this).load(it[position].imageUrl).into(binding.imgSong)
            Log.e("current", it[position].toString())

            switchViewPagerToCurrentSong(it[position])
        }

        viewModel.mutableLiveDataSong.observe(this) {
            mediaPlayer.reset()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            mediaPlayer.setDataSource(it[position].songUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                togglePlayBack()
                mediaPlayer.setOnErrorListener { _, what, extra ->
                    println("MediaPlayer error: what=$what, extra=$extra")
                    false
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

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
                    binding.vpSong.visibility = View.INVISIBLE
                    binding.imgCard.visibility = View.INVISIBLE
                    binding.playButton.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun viewPagerVisible() {
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
}
