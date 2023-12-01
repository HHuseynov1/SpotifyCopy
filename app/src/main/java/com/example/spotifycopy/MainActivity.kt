package com.example.spotifycopy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.spotifycopy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomView()

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

                R.id.startListeningFragment2 -> {
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
}