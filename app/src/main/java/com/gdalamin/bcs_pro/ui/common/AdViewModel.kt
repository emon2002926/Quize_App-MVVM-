package com.gdalamin.bcs_pro.ui.common

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    
    private val _adViewState = MutableLiveData<AdView?>()
    val adViewState: LiveData<AdView?> = _adViewState
    
    init {
        // Initialize ad setup
        checkAndLoadAd()
    }
    
    private fun checkAndLoadAd() {
        val lastAdShowTime = sharedPreferences.getLong("last_ad_show_time", 0)
        val currentTime = System.currentTimeMillis()
        
        // Check if we need to show a new ad
        if (currentTime - lastAdShowTime > 24 * 60 * 60 * 1000) { // 24 hours
            // Reset daily ad counter
            sharedPreferences.edit().putInt("daily_ad_count", 0).apply()
        }
        
        val dailyAdCount = sharedPreferences.getInt("daily_ad_count", 0)
        
        // Check if the daily limit has been reached
        if (dailyAdCount < 5) {
            // Load a new ad
            loadAd()
            // Update the last ad show time
            sharedPreferences.edit().putLong("last_ad_show_time", currentTime).apply()
            // Increment the daily ad count
            sharedPreferences.edit().putInt("daily_ad_count", dailyAdCount + 1).apply()
        } else {
            // Display a placeholder or empty view
            _adViewState.postValue(null)
        }
    }
    
    private fun loadAd() {
        // Create an AdView and set up AdMob
        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId =
                "ca-app-pub-3940256099942544/9214589741" // Replace with your actual AdMob Unit ID
            loadAd(AdRequest.Builder().build())
        }
        _adViewState.postValue(adView)
    }
    
    // Cleanup
    fun cleanup() {
        _adViewState.value?.destroy()
    }
}
