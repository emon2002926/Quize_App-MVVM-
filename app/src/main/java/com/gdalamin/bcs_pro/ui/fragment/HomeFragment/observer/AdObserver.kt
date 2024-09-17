package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer

import android.view.View
import android.view.ViewGroup
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.HomeFragment
import com.gdalamin.bcs_pro.ui.utilities.DataState

class AdObserver(
    private val fragment: HomeFragment,
    private val binding: FragmentHomeBinding,
    private val adViewModel: AdViewModel,
) {
    fun showBannerAd() = binding.apply {
        adViewModel.bannerAdState.observe(fragment.viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    var adView = dataState.data
                    adView?.let {
                        // Remove the adView from its current parent if necessary
                        (it.parent as? ViewGroup)?.removeView(it)
                        binding.adContainer.removeAllViews() // Clear any previous ad
                        binding.adContainer.addView(it) // Add the new ad
                    }
                }
                
                is DataState.Error -> {
                    binding.adContainer.visibility = View.GONE
                }
                
                is DataState.Loading -> TODO()
            }
        }
    }
    
}