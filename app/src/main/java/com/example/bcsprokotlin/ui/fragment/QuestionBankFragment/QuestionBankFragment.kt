package com.example.bcsprokotlin.ui.fragment.QuestionBankFragment

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.adapter.QuestionBankAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBankBinding
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuestionBankFragment :
    BaseFragment<FragmentQuestionBankBinding>(FragmentQuestionBankBinding::inflate) {

    private val viewModel: QuestionBankViewModel by viewModels()

    @Inject
    lateinit var questionBankAdapter: QuestionBankAdapter

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
                        questionBankAdapter.differ.submitList(it)
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


}