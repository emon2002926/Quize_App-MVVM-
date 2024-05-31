package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBinding
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate) {
    private var questionAdapter = QuestionAdapter()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val viewModel: QuestionViewModel by viewModels()

    override fun onCreateView() {


        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        sharedViewModel.sharedData.observe(viewLifecycleOwner, { data ->
            viewModel.viewModelScope.launch {

                when (data.action) {
                    "normalExam" -> viewModel.getExamQuestions(data.totalQuestion)
                    "questionBank" -> viewModel.getPreviousYearQuestions(
                        200,
                        data.batchOrSubjectName
                    )

                    "subjectBasedQuestions" -> viewModel.getSubjectExamQuestions(
                        data.batchOrSubjectName,
                        data.totalQuestion
                    )

                }


            }
        })


        viewModel.questions.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
                    response.data?.let { questionList ->
                        questionAdapter.submitList(questionList)
                        GeneralUtils.hideShimmerLayout(
                            binding.shimmerLayout,
                            binding.rvQuestion
                        )

                    }
                }

                is Resource.Error -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        setupRecyclerView()
    }


    private fun setupRecyclerView() = binding.rvQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)
    }


}