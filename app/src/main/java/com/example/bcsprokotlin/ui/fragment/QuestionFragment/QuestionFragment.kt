package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.R
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
    private var mBooleanValue = false

    override fun loadUi() {

        setupUI()
        observeSharedData()
        observeQuestions()
    }


    private fun setupUI() = binding.apply {
        backButton.setOnClickListener { findNavController().navigateUp() }
        btnShowAnswer.setOnClickListener {
            mBooleanValue = !mBooleanValue
            questionAdapter.showAnswer(mBooleanValue)

            if (mBooleanValue) {
                btnShowAnswer.setImageResource(R.drawable.show_answer)
            } else {
                btnShowAnswer.setImageResource(R.drawable.hide_answer)
            }
        }
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
                changeUiForExam("time")
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "liveModelTest" -> {
                changeUiForExam("time")
                tvTitle.text = data.title
                tvTimer.visibility = View.VISIBLE
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "questionBank" -> {
                tvTitle.text = data.batchOrSubjectName
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
            }

            "subjectBasedExam" -> {
                tvTitle.text = data.title
                changeUiForExam("time")
                viewModel.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
            }

            "subjectBasedQuestions" -> {
                tvTitle.text = data.title
                questionAdapter.changeUiForExam("normalQuestion")
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
                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(
                        binding.shimmerLayout,
                        binding.rvQuestion
                    )
                    binding.btnShowAnswer.visibility = View.GONE
                }

                is Resource.Success -> {
                    handleSuccess(response.data)
                    binding.btnShowAnswer.visibility = View.VISIBLE
                }

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

    fun changeUiForExam(time: String) = binding.apply {
        tvTimer.visibility = View.VISIBLE
        btnShowAnswer.setImageResource(R.drawable.baseline_check_24)
        questionAdapter.changeUiForExam("examQuestion")
    }

}