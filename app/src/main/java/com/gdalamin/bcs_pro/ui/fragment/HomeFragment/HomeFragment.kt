package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.view.View
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
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_BANNER_AD_ID
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_INTERSTITIAL_LIVE_EXAM_AD_ID
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
    private val adViewModel: AdViewModel by activityViewModels()
    private lateinit var networkReceiverManager: NetworkReceiverManager
    private lateinit var homeFragmentObserver: HomeFragmentObserver
    private lateinit var interstitialAdObserver: InterstitialAdObserver<HomeFragment, FragmentHomeBinding>
    private lateinit var interstitialAdObserver1: InterstitialAdObserver<HomeFragment, FragmentHomeBinding>
    private var adView: AdView? = null
    
    
    override fun loadUi() {
        
        initializeNetworkReceiver()
        initializeObservers()
        setupClickListeners()
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)
        networkCall()
        handleBackPress()
        
        adViewModel.loadBannerAd(ADMOB_BANNER_AD_ID)
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
            adUnitId = ADMOB_INTERSTITIAL_LIVE_EXAM_AD_ID,
            navigateAction = {
                findNavController().navigate(R.id.action_homeFragment_to_examFragment)
            }
        )
        
        
        binding.apply {
            setSubjectClickListener(tvInternationalAffairs, "আন্তর্জাতিক বিষয়াবলি", "IA")
            setSubjectClickListener(tvBangladeshAffairs, "বাংলাদেশ বিষয়াবলি", "BA")
            setSubjectClickListener(tvGeography, "ভূগোল", "GEDM")
        }
    }
    
    
    private fun setSubjectClickListener(view: View, subjectName: String, subjectCode: String) {
        view.setOnClickListener {
            showSubjectBasedQuestion(subjectName, subjectCode)
        }
    }
    
    private fun showSubjectBasedQuestion(subjectName: String, subjectCode: String) {
        val data = SharedData(
            title = subjectName,
            action = "subjectBasedQuestions",
            totalQuestion = 200,
            questionType = "",
            batchOrSubjectName = subjectCode,
            time = 0
        )
        
        sharedViewModel.setSharedData(data)
//        interstitialAdObserver1.observeInterstitialAd()
        findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
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
