package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.util.Log
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
import com.example.bcsprokotlin.ui.fragment.ExamFragment.ResultViewModel
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate),
    QuestionAdapter.OnItemSelectedListener {
    private val resultViewModel: ResultViewModel by viewModels()

    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private var mBooleanValue = false

    private var questionLists = ArrayList<Question>()

    override fun loadUi() {

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        observeSharedData()
        observeQuestions()
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

            "questionBank" -> {
                tvTitle.text = data.batchOrSubjectName
                setupFab("normalQuestion")
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
            }

            "subjectBasedQuestions" -> {
                tvTitle.text = data.title
                setupFab("normalQuestion")
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


    private fun setupFab(action: String) = binding.apply {
        when (action) {
            "normalQuestion" -> {
                tvTimer.visibility = View.GONE
                btnShowAnswer.setOnClickListener {
                    mBooleanValue = !mBooleanValue
                    questionAdapter.showAnswer(mBooleanValue)
                    if (mBooleanValue) {
                        btnShowAnswer.setImageResource(R.drawable.show_answer)
                    } else {
                        btnShowAnswer.setImageResource(R.drawable.hide_answer)
                    }
                }
            }

            "normalExam" -> {
                tvTimer.visibility = View.VISIBLE
                btnShowAnswer.setImageResource(R.drawable.baseline_check_24)
                btnShowAnswer.visibility = View.VISIBLE
                questionAdapter.changeUiForExam("examQuestion")
            }
        }

    }

    override fun onItemSelected(item: Question) {
        questionLists.add(item)
//        viewModel.submitAnswer("50QuestionExam", questionLists)

    }


    fun logger(message: String) = Log.d("QuestionFragmentLog", message)


}