package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer

import androidx.navigation.fragment.findNavController
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.HomeFragment
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.HomeFragmentViewModel
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback

class HomeFragmentObserver(
    private val fragment: HomeFragment,
    private val homeFragmentViewModel: HomeFragmentViewModel,
    private val adViewModel: AdViewModel,
    private val binding: FragmentHomeBinding,
    private val liveExamAdapter: LiveExamAdapter
) {
    fun observeLiveData() {
        observeLiveExamInfo()
//        observeAdState()
    }
    
    private fun observeLiveExamInfo() = binding.apply {
        // Observe network call results
        homeFragmentViewModel.liveExamInfo.observe(fragment.viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
                    showShimmerLayout(shimmerLiveExam, rvLiveExam)
                    swipeRefreshLayout.isRefreshing = false
                }
                
                is DataState.Loading -> {
                    showShimmerLayout(shimmerLiveExam, rvLiveExam)
                }
                
                is DataState.Success -> {
                    hideShimmerLayout(shimmerLiveExam, rvLiveExam)
                    swipeRefreshLayout.isRefreshing = false
                    response.data?.let {
                        liveExamAdapter.submitList(it)
                    }
                }
            }
        }
    }
    
    fun observeInterstitialAd() {
        val adListener = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
                adViewModel.reloadInterstitialAd() // Preload the next ad
            }
            
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
            }
            
            override fun onAdShowedFullScreenContent() {
            }
        }
        
        adViewModel.adState.observe(fragment.viewLifecycleOwner) { adState ->
            when (adState) {
                is DataState.Loading -> {
                }
                
                is DataState.Success -> {
                    // Show the preloaded ad when the user triggers an action
                    fragment.activity?.let {
                        adViewModel.showInterstitialAd(it, adListener)
                    }
                }
                
                is DataState.Error -> {
                }
            }
        }
        // Check if an ad is already loaded
        if (adViewModel.adState.value is DataState.Success) {
            // If an ad is preloaded, show it immediately
            fragment.activity?.let {
                adViewModel.showInterstitialAd(it, adListener)
            }
        } else {
            // If no ad is loaded, preload it and navigate to the next fragment
            adViewModel.preloadInterstitialAd()
            fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
        }
        
    }
    
    
}