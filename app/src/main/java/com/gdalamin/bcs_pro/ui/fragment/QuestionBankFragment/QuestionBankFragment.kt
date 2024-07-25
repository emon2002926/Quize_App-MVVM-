package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.adapter.QuestionBankAdapter
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBankBinding
import com.gdalamin.bcs_pro.model.BcsYearName
import com.gdalamin.bcs_pro.model.SharedData
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.util.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.util.GeneralUtils.showShimmerLayout
import com.gdalamin.bcs_pro.util.Resource
import com.gdalamin.bcs_pro.util.network.NetworkReceiverManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate),
    QuestionBankAdapter.HandleClickListener, NetworkReceiverManager.ConnectivityChangeListener {

    private val viewModel: QuestionBankViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val questionBankAdapter = QuestionBankAdapter(this)
    private lateinit var networkReceiverManager: NetworkReceiverManager

    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        observeQuestionYearName()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
    }

    private fun observeQuestionYearName() {
        viewModel.bcsYearName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    showShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                }

                is Resource.Loading -> {
                    showShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                }

                is Resource.Success -> {
                    hideShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                    response.data?.let {
                        questionBankAdapter.submitList(it)
                    }
                }
            }
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
            totalQuestion = 50, questionType = "",
            batchOrSubjectName = bcsYearName.bcsYearName,
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
        observeQuestionYearName()
    }

    override fun onDisconnected() {
    }


}