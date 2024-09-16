package com.gdalamin.bcs_pro

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.gdalamin.bcs_pro.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //    private lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        firebaseAnalytics = Firebase.analytics
        
        val navController = findNavController(R.id.newsNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)


//         Handle item selection events
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Navigate to homeFragment2 when "Home" item is selected
                    navController.navigate(R.id.homeFragment)
                    true
                }
                
                R.id.questionFragment -> {
                    // Navigate to questionFragment and hide the bottom navigation view
                    navController.navigate(R.id.questionFragment)
                    hideBottomNavigationView()
                    true
                }
                
                R.id.examFragment -> {
                    navController.navigate(R.id.examFragment)
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
            } else if (destination.id == R.id.examFragment) {
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