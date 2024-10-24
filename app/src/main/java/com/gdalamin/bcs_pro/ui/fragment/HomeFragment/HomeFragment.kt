package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.common.observer.BannerAdObserver
import com.gdalamin.bcs_pro.ui.common.observer.InterstitialAdObserver
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout.NotificationLayout
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout.NotificationViewModel
import com.gdalamin.bcs_pro.ui.fragment.HomeFragment.observer.HomeFragmentObserver
import com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment.SubjectViewModel
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_BANNER_AD_TEST_ID
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_INTERSTITIAL_AD_TEST_ID
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.rewarded.RewardItem
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
    private val adViewModel: AdViewModel by activityViewModels()
    private lateinit var networkReceiverManager: NetworkReceiverManager
    private lateinit var homeFragmentObserver: HomeFragmentObserver
    private lateinit var interstitialAdObserver: InterstitialAdObserver<HomeFragment, FragmentHomeBinding>
    private var adView: AdView? = null
    
    override fun loadUi() {
        
        initializeNetworkReceiver()
        initializeObservers()
        setupClickListeners()
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)
        networkCall()
        handleBackPress()
        
        adViewModel.loadBannerAd(ADMOB_BANNER_AD_TEST_ID)
        val adObserver = BannerAdObserver(
            fragment = this,
            binding = binding,
            adContainer = binding.adContainer,
            adViewModel = adViewModel
        )
        
        adObserver.showBannerAd()
        
        interstitialAdObserver = InterstitialAdObserver(
            fragment = this,
            adViewModel = adViewModel,
            binding = binding,
            adUnitId = ADMOB_INTERSTITIAL_AD_TEST_ID,
            navigateAction = {
                findNavController().navigate(R.id.action_homeFragment_to_examFragment)
            }
        )

//        val rewardedAdObserver = RewardedAdObserver(
//            fragment = this,
//            adViewModel = adViewModel,
//            binding = binding,
//            adUnitId = "ca-app-pub-3940256099942544/5224354917",
//            showResulView = { navigateToNextScreen() }, // Define your navigation logic here
//            onUserEarnedReward = { rewardItem ->
//                handleUserReward(rewardItem) // Handle what happens when the user earns the reward
//            }
//        )
//        binding.btnQuestionBank.setOnClickListener {
////            rewardedAdObserver.observeRewardedAd()
//
//        }
        
        // Observe the ad
        
        
    }
    
    private fun navigateToNextScreen() {
        // For example, use NavController or any other method to navigate
        findNavController().navigate(R.id.action_homeFragment_to_questionBankFragment)
    }
    
    private fun handleUserReward(rewardItem: RewardItem) {
        // Example: Show a toast or update UI with the reward
        Toast.makeText(
            requireContext(),
            "User earned ${rewardItem.amount} ${rewardItem.type}",
            Toast.LENGTH_SHORT
        ).show()
        
        // You can add more logic here depending on the reward
    }
    
    
    private fun initializeObservers() {
        homeFragmentObserver = HomeFragmentObserver(
            this, homeFragmentViewModel, binding, liveExamAdapter
        )
        homeFragmentObserver.observeLiveData()
    }
    
    private fun initializeNetworkReceiver() {
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
    }
    
    private fun setupClickListeners() {
        HomeFragmentClickListener(
            this@HomeFragment, binding, sharedViewModel, homeFragmentViewModel
        )
        NotificationLayout(this@HomeFragment, notificationViewModel, binding)
        binding.horizontalScrollView.isHorizontalScrollBarEnabled = false
    }
    
    
    private fun networkCall() {
        
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
        interstitialAdObserver.observeInterstitialAd()
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
