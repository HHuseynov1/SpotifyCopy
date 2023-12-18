package com.example.spotifycopy

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.spotifycopy.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        Thread.sleep(3000)
//        val w = window
//        w.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        installSplashScreen()

        bottomView()
        navigationViewAccess()

        setContentView(binding.root)
    }

    fun openDrawer(){
        binding.drawerLayout.openDrawer( GravityCompat.START )
        navigationView()
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
                R.id.login -> {
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
