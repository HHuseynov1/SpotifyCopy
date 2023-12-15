package com.example.spotifycopy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.spotifycopy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        bottomView()
        navigationView()

        setContentView(binding.root)
    }

    private fun bottomView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.getStartedFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.createEmailFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.createPasswordFargment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.selectGenderFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.startListeningFragmentArtists ->{
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.startListeningFragmentEnd -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.login -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                R.id.homeFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.searchFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.libraryFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigationView(){
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.whatsNew -> Toast.makeText(this,"What's New", Toast.LENGTH_LONG).show()
                R.id.listeningHistory -> Toast.makeText(this,"Listening History", Toast.LENGTH_LONG).show()
                R.id.settings -> Toast.makeText(this,"Settings", Toast.LENGTH_LONG).show()
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}