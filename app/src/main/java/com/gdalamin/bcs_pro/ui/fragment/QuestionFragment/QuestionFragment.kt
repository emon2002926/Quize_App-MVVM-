package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.QuestionAdapterPaging
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.LoadingStateAdapter
import com.gdalamin.bcs_pro.ui.fragment.ExamFragment.ExamViewModel
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate),
    NetworkReceiverManager.ConnectivityChangeListener {

    private val questionAdapterPaging by lazy { QuestionAdapterPaging() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ExamViewModel by viewModels()
    private var mBooleanValue = false

    private lateinit var networkReceiverManager: NetworkReceiverManager

    private val testViewModel: QuestionViewMode by viewModels()

    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        observeSharedData()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
    }

    private fun observeSharedData() = binding.apply {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            viewModel.viewModelScope.launch {
                when (data.action) {
                    "questionBank" -> {
                        tvTitle.text = data.title
                        setupFab()
                        testViewModel.getQuestions(9, data.batchOrSubjectName)
                        observePagingQuestions()
                    }

                    "subjectBasedQuestions" -> {
                        tvTitle.text = data.title
                        setupFab()
                        testViewModel.getQuestions(10, data.batchOrSubjectName)
                        observePagingQuestions()
                    }

                    "importantQuestion" -> {
                        tvTitle.text = data.title
                        setupFab()
                        testViewModel.getQuestions(1)
                        observePagingQuestions()
                    }
                }
            }
        }
    }

    private fun observePagingQuestions() {
        lifecycleScope.launch {
            testViewModel.questions.collectLatest { pagingData ->
                questionAdapterPaging.submitData(pagingData)
            }
        }
        questionAdapterPaging.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    binding.shimmerLayout.startShimmer()
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.rvQuestion.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvQuestion.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.rvQuestion.apply {
        adapter = questionAdapterPaging.withLoadStateFooter(
            footer = LoadingStateAdapter { questionAdapterPaging.retry() }
        )
        layoutManager = LinearLayoutManager(context)
    }

    private fun setupFab() = binding.apply {
        btnShowAnswer.setOnClickListener {
            mBooleanValue = !mBooleanValue
            questionAdapterPaging.showAnswer(mBooleanValue)
            if (mBooleanValue) {
                btnShowAnswer.setImageResource(R.drawable.show_answer)
            } else {
                btnShowAnswer.setImageResource(R.drawable.hide_answer)
            }
        }
    }

    override fun onConnected() {
        questionAdapterPaging.retry()
    }

    override fun onDisconnected() {}

    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
    }
}