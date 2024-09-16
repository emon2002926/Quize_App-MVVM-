package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import androidx.navigation.fragment.findNavController
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.utilities.DataState
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.showShimmerLayout
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
    
    fun adObserver() {
        val adListener = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Navigate to the new fragment after the ad is closed
                fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
                adViewModel.reloadInterstitialAd() // Preload the next ad
            }
            
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Navigate immediately if the ad fails
                fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
            }
            
            override fun onAdShowedFullScreenContent() {
                // Ad has been shown
            }
        }
        
        adViewModel.adState.observe(fragment.viewLifecycleOwner) { adState ->
            when (adState) {
                is DataState.Loading -> {
                    // Optionally, show a loading indicator
                    fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
                    
                }
                
                is DataState.Success -> {
                    // Show the preloaded ad when the user triggers an action
                    fragment.activity?.let {
                        adViewModel.showInterstitialAd(it, adListener)
                    }
                }
                
                is DataState.Error -> {
                    fragment.findNavController().navigate(R.id.action_homeFragment_to_examFragment)
                }
            }
        }
        
        
    }
    
}