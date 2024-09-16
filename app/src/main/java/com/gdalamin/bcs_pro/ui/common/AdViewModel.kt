package com.gdalamin.bcs_pro.ui.common

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdalamin.bcs_pro.ui.utilities.DataState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    
    private var interstitialAd: InterstitialAd? = null
    private val _adState = MutableLiveData<DataState<InterstitialAd>>()
    val adState: LiveData<DataState<InterstitialAd>> = _adState
    
    
    fun preloadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            getApplication<Application>().applicationContext,
            "ca-app-pub-3940256099942544/1033173712", // Replace with your actual AdMob Interstitial Ad Unit ID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    _adState.postValue(DataState.Success(ad)) // Notify that ad is loaded
                }
                
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    _adState.postValue(DataState.Error("Failed to load interstitial ad"))
                }
            })
    }
    
    fun showInterstitialAd(activity: Activity, adListener: FullScreenContentCallback) {
        interstitialAd?.fullScreenContentCallback = adListener
        interstitialAd?.show(activity) ?: run {
            _adState.postValue(DataState.Error("Interstitial ad is not loaded"))
        }
    }
    
    // Reload the ad after showing
    fun reloadInterstitialAd() {
        preloadInterstitialAd()
    }
}
