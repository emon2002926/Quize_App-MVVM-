package com.gdalamin.bcs_pro.ui.common

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_BANNER_AD_TEST_ID
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_INTERSTITIAL_AD_TEST_ID
import com.gdalamin.bcs_pro.utilities.DataState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
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
    
    // Banner Ad related LiveData
    private val _bannerAdState = MutableLiveData<DataState<AdView>>()
    val bannerAdState: LiveData<DataState<AdView>> = _bannerAdState
    
    // Load Banner Ad
// Load Banner Ad
    fun loadBannerAd(context: Context) {
        val adView = AdView(context).apply {
            adUnitId = ADMOB_BANNER_AD_TEST_ID
            setAdSize(AdSize.BANNER) // Set AdSize properly during initialization
        }
        
        val adRequest = AdRequest.Builder().build()
        
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                _bannerAdState.postValue(DataState.Success(adView)) // Notify that banner ad is loaded
            }
            
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                _bannerAdState.postValue(DataState.Error("Failed to load banner ad"))
            }
        }
    }
    
    // Load Interstitial Ad
    fun preloadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            getApplication<Application>().applicationContext,
            ADMOB_INTERSTITIAL_AD_TEST_ID, // Replace with your actual AdMob Interstitial Ad Unit ID
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
