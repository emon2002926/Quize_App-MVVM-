package com.example.bcsprokotlin.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.newsNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)


        // Handle item selection events
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment2 -> {
                    // Navigate to homeFragment2 when "Home" item is selected
                    navController.navigate(R.id.homeFragment2)
                    true
                }
                R.id.questionFragment -> {
                    // Navigate to questionFragment and hide the bottom navigation view
                    navController.navigate(R.id.questionFragment)
                    hideBottomNavigationView()
                    true
                }
                else -> {
                    // Delegate navigation action to NavigationUI
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
        }

        // Set listener to control visibility of bottom navigation view
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.questionFragment) {
                hideBottomNavigationView()
            } else {
                showBottomNavigationView()
            }
        }
    }

    private fun hideBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }



}