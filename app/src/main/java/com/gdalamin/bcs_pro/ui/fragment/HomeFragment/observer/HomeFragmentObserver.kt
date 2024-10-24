package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer

import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.HomeFragment
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.HomeFragmentViewModel
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout

class HomeFragmentObserver(
    private val fragment: HomeFragment,
    private val homeFragmentViewModel: HomeFragmentViewModel,
    private val binding: FragmentHomeBinding,
    private val liveExamAdapter: LiveExamAdapter
) {
    fun observeLiveData() {
        observeLiveExamInfo()
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
    
    
}