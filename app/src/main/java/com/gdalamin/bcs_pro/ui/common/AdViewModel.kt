package com.gdalamin.bcs_pro.ui.common

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdalamin.bcs_pro.utilities.DataState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    
    private var interstitialAd: InterstitialAd? = null
    var rewardedAd: RewardedAd? = null
    
    private val _adState = MutableLiveData<DataState<InterstitialAd>>()
    val adState: LiveData<DataState<InterstitialAd>> = _adState
    
    private val _bannerAdState = MutableLiveData<DataState<AdView>>()
    val bannerAdState: LiveData<DataState<AdView>> = _bannerAdState
    
    private val _rewardedAdState = MutableLiveData<DataState<RewardedAd>>()
    val rewardedAdState: LiveData<DataState<RewardedAd>> = _rewardedAdState
    
    
    fun loadBannerAd(adUnitId: String) {
        val adView = AdView(getApplication<Application>().applicationContext).apply {
            this.adUnitId = adUnitId
            setAdSize(AdSize.BANNER)
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
    fun preloadInterstitialAd(adUnitId: String) {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            getApplication<Application>().applicationContext,
            adUnitId,
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
    fun reloadInterstitialAd(adUnitId: String) {
        preloadInterstitialAd(adUnitId)
    }
    
    
    // Load Rewarded Ad
    fun loadRewardedAd(adUnitId: String) {
        val adRequest = AdRequest.Builder().build()
        
        RewardedAd.load(
            getApplication<Application>().applicationContext,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    _rewardedAdState.postValue(DataState.Success(ad)) // Notify that rewarded ad is loaded
                }
                
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    _rewardedAdState.postValue(DataState.Error("Failed to load rewarded ad"))
                }
            }
        )
    }
    
    // Show Rewarded Ad
    fun showRewardedAd(activity: Activity, onUserEarnedReward: OnUserEarnedRewardListener) {
        rewardedAd?.show(activity, onUserEarnedReward) ?: run {
            _rewardedAdState.postValue(DataState.Error("Rewarded ad is not loaded"))
        }
    }
    
    // Reload the rewarded ad after showing
    fun reloadRewardedAd(adUnitId: String) {
        loadRewardedAd(adUnitId)
    }
    
    
}
