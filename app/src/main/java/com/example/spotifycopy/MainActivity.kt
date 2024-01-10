package com.example.spotifycopy

import android.graphics.BlurMaskFilter.Blur
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.spotifycopy.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value as Boolean
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        bottomView()
        navigationViewAccess()
        navigationView()

        setContentView(binding.root)
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
