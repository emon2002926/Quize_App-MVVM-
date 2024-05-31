package com.example.bcsprokotlin.ui.fragment.QuestionBankFragment

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.QuestionBankAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBankBinding
import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate),
    QuestionBankAdapter.HandleClickListener {

    private val viewModel: QuestionBankViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val questionBankAdapter = QuestionBankAdapter(this)

    override fun onCreateView() {

        binding.backButton.setOnClickListener { findNavController().navigateUp() }


        viewModel.bcsYearName.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Error -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestionBank)
                    response.data?.let {
                        questionBankAdapter.submitList(it)
                    }
                }
            }

        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() = binding.rvQuestionBank.apply {
        adapter = questionBankAdapter
        layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(bcsYearName: BcsYearName) {

        val data = SharedData("", "questionBank", 50, "", bcsYearName.bcsYearName, 0)

        sharedViewModel.setSharedData(data)
        findNavController().navigate(R.id.action_questionBankFragment_to_questionFragment)

    }


}