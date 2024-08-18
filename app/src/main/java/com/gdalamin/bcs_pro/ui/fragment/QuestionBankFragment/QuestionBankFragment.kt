package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBankBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.QuestionBankAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.showShimmerLayout
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate),
    QuestionBankAdapter.HandleClickListener, NetworkReceiverManager.ConnectivityChangeListener {

    //    private val viewModel: QuestionBankViewModel by viewModels()
    private val viewModelTest: QuestionBankViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val questionBankAdapter = QuestionBankAdapter(this)
    private lateinit var networkReceiverManager: NetworkReceiverManager

    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

//        observeQuestionYearName()
        observeBcsYearName()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
    }


    private fun observeBcsYearName() = binding.apply {
        // Observe data from Room database
        viewModelTest.bcsYearName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    showShimmerLayout(shimmerLayout, rvQuestionBank)
                }

                is Resource.Loading -> {
                    showShimmerLayout(shimmerLayout, rvQuestionBank)
                }

                is Resource.Success -> {
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
//        observeQuestionYearName()
    }

    override fun onDisconnected() {
    }


}