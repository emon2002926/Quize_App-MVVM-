package com.gdalamin.bcs_pro.ui.common.observer

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdObserver<T>(
    private val activity: Activity,
    private val adUnitId: String,
    private val rewardedAdListener: RewardedAdListener // Listener for ad events
) {
    var rewardedAd: RewardedAd? = null
    
    // Load RewardedAd
    fun preloadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        
        RewardedAd.load(
            activity,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // Notify listener that the ad was closed
                            rewardedAdListener.onAdClosed()
                        }
                        
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Notify listener that the ad failed to show
                            rewardedAdListener.onAdFailedToShow(adError)
                        }
                        
                        override fun onAdShowedFullScreenContent() {
                            // The ad is now showing, set the rewardedAd to null to allow future reloads
                            rewardedAd = null
                        }
                    }
                }
                
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Notify listener that the ad failed to load
                    rewardedAdListener.onAdFailedToLoad(loadAdError)
                }
            }
        )
    }
    
    // Show the preloaded ad
    fun showAd() {
        rewardedAd?.show(activity) { rewardItem ->
            // Handle reward logic if necessary
        } ?: run {
            // If ad is not available, trigger ad failure
            rewardedAdListener.onAdFailedToShow(
                AdError(
                    0,
                    "No ad loaded",
                    "com.google.android.gms.ads"
                )
            )
        }
    }
    
    interface RewardedAdListener {
        fun onAdClosed()
        fun onAdFailedToShow(adError: AdError)
        fun onAdFailedToLoad(loadAdError: LoadAdError)
    }
}