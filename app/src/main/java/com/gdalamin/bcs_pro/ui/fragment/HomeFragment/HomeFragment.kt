package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout.NotificationLayout
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout.NotificationViewModel
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer.AdObserver
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer.HomeFragmentObserver
import com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment.SubjectViewModel
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    LiveExamAdapter.HandleClickListener,
    NetworkReceiverManager.ConnectivityChangeListener {
    
    private val homeFragmentViewModel: HomeFragmentViewModel by activityViewModels()
    private val subjectViewModel: SubjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val liveExamAdapter by lazy { LiveExamAdapter(this) }
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val adViewModel: AdViewModel by viewModels()
    private lateinit var networkReceiverManager: NetworkReceiverManager
    private lateinit var homeFragmentObserver: HomeFragmentObserver
    
    private lateinit var adObserver: AdObserver
    private var adView: AdView? = null
    
    override fun loadUi() {
        
        initializeNetworkReceiver()
        initializeObservers()
        setupClickListeners()
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)
        networkCall()
        handleBackPress()
        
        
        adObserver = AdObserver(this@HomeFragment, binding, adViewModel)
        
        adObserver.showBannerAd()
        
        adViewModel.loadBannerAd(requireContext())
        
    }
    
    
    private fun initializeObservers() {
        homeFragmentObserver = HomeFragmentObserver(
            this, homeFragmentViewModel, adViewModel, binding, liveExamAdapter
        )
        homeFragmentObserver.observeLiveData()
    }
    
    private fun initializeNetworkReceiver() {
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
        adViewModel.preloadInterstitialAd()
    }
    
    private fun setupClickListeners() {
        HomeFragmentClickListener(
            this@HomeFragment, binding, sharedViewModel, homeFragmentViewModel
        )
        NotificationLayout(this@HomeFragment, notificationViewModel, binding)
        binding.horizontalScrollView.isHorizontalScrollBarEnabled = false
    }
    
    
    private fun networkCall() {
//        if (isInternetAvailable(requireContext())) {
//            homeFragmentViewModel.clearDatabaseIfNeededTime()
//        }
        homeFragmentViewModel.getExamInfo()
        subjectViewModel.getSubjectsName()
    }
    
    
    override fun onClickLiveExam(item: LiveExam) {
        val data = SharedData(
            title = item.examTitle,
            action = "liveExam",
            totalQuestion = item.totalQc,
            questionType = item.questionSet,
            batchOrSubjectName = "",
            time = item.time * 60
        
        )
        
        sharedViewModel.setSharedData(data)
//        findNavController().navigate(R.id.action_homeFragment_to_examFragment)
        
        homeFragmentObserver.observeInterstitialAd()
    }
    
    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    
    
    private fun handleBackPress() {
        val backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                CustomConfirmationDialog(this@HomeFragment).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
        
    }
    
    override fun onConnected() {
        homeFragmentObserver.observeLiveData()
        networkCall()
    }
    
    override fun onDisconnected() {
    }
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
        adView?.destroy() // Clean up the adView
        
    }
    
}
