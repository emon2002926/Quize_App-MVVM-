package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBankBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.QuestionBankAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.common.observer.InterstitialAdObserver
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_INTERSTITIAL_QUESTION_BANK_AD_ID
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate),
    QuestionBankAdapter.HandleClickListener, NetworkReceiverManager.ConnectivityChangeListener {
    
    private val questionBankViewModel: QuestionBankViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val adViewModel: AdViewModel by activityViewModels()
    private val questionBankAdapter = QuestionBankAdapter(this)
    private lateinit var networkReceiverManager: NetworkReceiverManager
    lateinit var interstitialAdObserver: InterstitialAdObserver<QuestionBankFragment, FragmentQuestionBankBinding>
    
    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        
        observeBcsYearName()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
        
        interstitialAdObserver = InterstitialAdObserver(
            fragment = this,
            adViewModel = adViewModel,
            binding = binding,
            adUnitId = ADMOB_INTERSTITIAL_QUESTION_BANK_AD_ID, // Pass your ad unit ID here
            navigateAction = {
                findNavController().navigate(R.id.action_questionBankFragment_to_questionFragment) // Navigation action
            }
        )
    }
    
    private fun observeBcsYearName() = binding.apply {
        questionBankViewModel.bcsYearName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                
                is DataState.Loading -> {
                    showShimmerLayout(shimmerLayout, rvQuestionBank)
                }
                
                is DataState.Success -> {
                    hideShimmerLayout(shimmerLayout, rvQuestionBank)
                    response.data?.let {
                        questionBankAdapter.submitList(it)
                    }
                }
            }
        }
        
        questionBankViewModel.getBcsYearNameDatabase().observe(viewLifecycleOwner) { exams ->
            questionBankAdapter.submitList(exams)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            questionBankViewModel.getBcsYearName(apiNumber = 5)
        }
    }
    
    private fun setupRecyclerView() = binding.rvQuestionBank.apply {
        adapter = questionBankAdapter
        layoutManager = LinearLayoutManager(context)
    }
    
    override fun onClick(bcsYearName: BcsYearName) {
        val data = SharedData(
            title = bcsYearName.bcsYearName,
            action = "questionBank",
            totalQuestion = bcsYearName.totalQuestion.toInt(),
            batchOrSubjectName = bcsYearName.bcsYearName,
            isSavedOnDatabase = bcsYearName.isQuestionSaved
        )
        sharedViewModel.setSharedData(data)
        interstitialAdObserver.observeInterstitialAd()
    }
    
    override fun onClickDownload(bcsYearName: BcsYearName) {
        questionBankViewModel.markDownloadStarted(bcsYearName.id)
        
        questionBankViewModel.getYearQuestion(
            bcsYearName.id,
            bcsYearName.bcsYearName,
            bcsYearName.totalQuestion.toInt()
        )
        
        questionBankViewModel.downloadComplete.observe(viewLifecycleOwner) { id ->
            if (bcsYearName.id == id) {
                questionBankViewModel.markDownloadCompleted(id)
                bcsYearName.isDownloading = false
                questionBankAdapter.isAnyItemDownloading = false
                questionBankAdapter.notifyItemChanged(bcsYearName.id)
            }
        }
        
        questionBankViewModel.downloadError.observe(viewLifecycleOwner) { error ->
            error?.let {
                when (it) {
                    true -> {
                        Toast.makeText(
                            context,
                            "Download Failed, check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        questionBankAdapter.isAnyItemDownloading = false
                        questionBankAdapter.onDownloadFailed(bcsYearName)
                    }
                    
                    false -> questionBankAdapter.onDownloadComplete(bcsYearName)
                }
                questionBankViewModel.resetDownloadError()
            }
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Restore ongoing downloads
        questionBankViewModel.currentlyDownloading.observe(viewLifecycleOwner) { downloadingIds ->
            downloadingIds?.forEach { id ->
                val bcsYearName = questionBankAdapter.getItemById(id)
                bcsYearName?.let {
                    it.isDownloading = true
                    questionBankAdapter.isAnyItemDownloading = true
                    questionBankAdapter.notifyItemChanged(it.id)
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
    }
    
    override fun onConnected() {
        observeBcsYearName()
    }
    
    override fun onDisconnected() {
    }
}
