package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBinding
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate) {
    private val questionAdapter by lazy { QuestionAdapter() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()

    override fun onCreateView() {


        setupUI()
        observeSharedData()
        observeQuestions()
    }


    private fun setupUI() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        setupRecyclerView()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            viewModel.viewModelScope.launch {
                handleAction(data)
            }
        }
    }

    private suspend fun handleAction(data: SharedData) = binding.apply {
        when (data.action) {
            "normalExam" -> {
                tvTitle.text = data.title
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "liveModelTest" -> {
                tvTitle.text = data.title
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "questionBank" -> {
                tvTitle.text = data.batchOrSubjectName
                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
            }

            "subjectBasedQuestions" -> {
                tvTitle.text = data.title
                viewModel.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
            }
        }
    }

    private fun observeQuestions() {
        viewModel.questions.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> GeneralUtils.showShimmerLayout(
                    binding.shimmerLayout,
                    binding.rvQuestion
                )

                is Resource.Success -> handleSuccess(response.data)
                is Resource.Error -> handleError(response.message)
            }
        }
    }

    private fun handleSuccess(questionList: List<Question>?) {
        GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
        questionList?.let {
            questionAdapter.submitList(it)
        }
    }

    private fun handleError(message: String?) {
        GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
        message?.let {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() = binding.rvQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)
    }


}