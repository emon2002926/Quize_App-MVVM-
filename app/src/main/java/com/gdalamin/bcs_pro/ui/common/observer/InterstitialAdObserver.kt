package com.gdalamin.bcs_pro.ui.common.observer

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.utilities.DataState
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback

class InterstitialAdObserver<F : Fragment, B : ViewBinding>(
    private val fragment: F,
    private val adViewModel: AdViewModel,
    private val binding: B,
    private val adUnitId: String,
    private val navigateAction: () -> Unit // Lambda function for navigation
) {
    init {
        adViewModel.preloadInterstitialAd(adUnitId)
    }
    
    fun observeInterstitialAd() {
        val adListener = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                navigateAction() // Perform the navigation
                adViewModel.reloadInterstitialAd(adUnitId) // Preload the next ad
            }
            
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                navigateAction() // Navigate even if the ad fails
            }
            
            override fun onAdShowedFullScreenContent() {
                // You can handle any UI updates when the ad is shown, if needed
            }
        }
        
        adViewModel.adState.observe(fragment.viewLifecycleOwner) { adState ->
            when (adState) {
                is DataState.Loading -> {
                    // You can show a loading indicator if needed
                }
                
                is DataState.Success -> {
                    // Show the preloaded ad when triggered
                    fragment.activity?.let {
                        adViewModel.showInterstitialAd(it, adListener)
                    }
                }
                
                is DataState.Error -> {
                    // You can handle the error, such as hiding UI elements
                }
            }
        }
        
        // Check if an ad is already loaded
        if (adViewModel.adState.value is DataState.Success) {
            // Show the preloaded ad immediately if it's available
            fragment.activity?.let {
                adViewModel.showInterstitialAd(it, adListener)
            }
        } else {
            // Preload the ad and navigate if it's not loaded
            adViewModel.preloadInterstitialAd(adUnitId)
            navigateAction() // Navigate even if the ad is not loaded yet
        }
    }
}
