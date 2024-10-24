package com.gdalamin.bcs_pro.ui.common.observer

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.utilities.DataState

class BannerAdObserver<F : Fragment, B : ViewBinding>(
    private val fragment: F,
    private val binding: B,
    private val adContainer: ViewGroup,
    private val adViewModel: AdViewModel,
    private val lifecycleOwner: LifecycleOwner = fragment.viewLifecycleOwner
) {
    
    fun showBannerAd() {
//        adViewModel.loadBannerAd(adUnitId)
        
        adViewModel.bannerAdState.observe(lifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    val adView = dataState.data
                    adView?.let {
                        (it.parent as? ViewGroup)?.removeView(it)
                        adContainer.removeAllViews()
                        adContainer.addView(it)
                    }
                }
                
                is DataState.Error -> {
                    adContainer.visibility = View.GONE
                }
                
                is DataState.Loading -> {
                }
            }
        }
    }
    
}
