package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.adapter.QuestionAdapter
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBinding
import com.gdalamin.bcs_pro.model.Question
import com.gdalamin.bcs_pro.model.SharedData
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.util.GeneralUtils
import com.gdalamin.bcs_pro.util.Resource
import com.gdalamin.bcs_pro.util.network.NetworkReceiverManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate),
    QuestionAdapter.OnItemSelectedListener, NetworkReceiverManager.ConnectivityChangeListener {

    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private var mBooleanValue = false

    private lateinit var networkReceiverManager: NetworkReceiverManager

    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        observeSharedData()
        observeQuestions()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
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
                tvTitle.text = data.title
                setupFab()
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
            }

            "subjectBasedQuestions" -> {
                tvTitle.text = data.title
                setupFab()
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
            }

            "importantQuestion" -> {
                tvTitle.text = data.title
                setupFab()
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getQuestion()
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

                is Resource.Error -> {}
//                    handleError(response.message)
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


    private fun setupFab() = binding.apply {

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

    override fun onItemSelected(item: Question) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
    }

    override fun onConnected() {
        observeSharedData()
        observeQuestions()
    }

    override fun onDisconnected() {
    }


}