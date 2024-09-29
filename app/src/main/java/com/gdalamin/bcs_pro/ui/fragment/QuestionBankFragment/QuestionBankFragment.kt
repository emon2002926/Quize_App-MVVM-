package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

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
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate),
    QuestionBankAdapter.HandleClickListener, NetworkReceiverManager.ConnectivityChangeListener {
    private val viewModelTest: QuestionBankViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    private val questionBankAdapter = QuestionBankAdapter(this)
    private lateinit var networkReceiverManager: NetworkReceiverManager
    
    
    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        
        observeBcsYearName()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
//        networkReceiverManager.register()
    }
    
    
    private fun observeBcsYearName() = binding.apply {
        // Observe data from Room database
        viewModelTest.bcsYearName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
//                    showShimmerLayout(shimmerLayout, rvQuestionBank)
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
        viewModelTest.getBcsYearNameDatabase().observe(viewLifecycleOwner) { exams ->
            questionBankAdapter.submitList(exams)
        }
        // Check for data and decide to fetch from network or not
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelTest.getBcsYearName(apiNumber = 5)
        }
    }
    
    
    private fun setupRecyclerView() = binding.rvQuestionBank.apply {
        adapter = questionBankAdapter
        layoutManager = LinearLayoutManager(context)
    }
    
    override fun onClick(bscYearName: BcsYearName) {
        val data = SharedData(
            title = bscYearName.bcsYearName,
            action = "questionBank",
            totalQuestion = 50, questionType = "",
            batchOrSubjectName = bscYearName.bcsYearName,
            time = 0
        )
        sharedViewModel.setSharedData(data)
        findNavController().navigate(R.id.action_questionBankFragment_to_questionFragment)
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