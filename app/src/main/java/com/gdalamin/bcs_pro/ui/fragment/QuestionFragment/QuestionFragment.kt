package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.adapter.QuestionAdapter
import com.gdalamin.bcs_pro.adapter.QuestionAdapterPaging
import com.gdalamin.bcs_pro.databinding.FragmentQuestionBinding
import com.gdalamin.bcs_pro.model.Question
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.util.GeneralUtils
import com.gdalamin.bcs_pro.util.Resource
import com.gdalamin.bcs_pro.util.network.NetworkReceiverManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate),
    QuestionAdapter.OnItemSelectedListener, NetworkReceiverManager.ConnectivityChangeListener {

    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val questionAdapterPaging by lazy { QuestionAdapterPaging() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private var mBooleanValue = false

    private lateinit var networkReceiverManager: NetworkReceiverManager

    private val testViewModel: QuestionViewModelTest by viewModels()

    override fun loadUi() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        observeSharedData()
        observeQuestions()
        setupRecyclerView()
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)

    }

    private fun observeSharedData() = binding.apply {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            viewModel.viewModelScope.launch {
//                handleAction(data)
                when (data.action) {
                    "questionBank" -> {
                        tvTitle.text = data.title
                        setupFab()
//                questionAdapter.changeUiForExam("normalQuestion")
//                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
                        testViewModel.getQuestions(9, data.batchOrSubjectName)
                        observePagingQuestions()
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
                        testViewModel.getQuestions(1)
                        observePagingQuestions()
                    }
                }
            }
        }
    }


    private fun observePagingQuestions() {
        setupRecyclerViewPaging()

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
                    binding.progressBar.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvQuestion.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }

            if (loadState.append is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerViewPaging() {
        binding.rvQuestion.adapter = questionAdapterPaging.withLoadStateFooter(
            footer = LoadingStateAdapter { questionAdapterPaging.retry() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
    }

    override fun onConnected() {
        questionAdapterPaging.retry()

    }

    override fun onDisconnected() {

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

                is Resource.Error -> {
                    Log.d("bhkjhdfg", response.message.toString())
                }
            }
        }
    }

    private fun handleSuccess(questionList: List<Question>?) {
        GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvQuestion)
        questionList?.let {
            questionAdapter.submitList(it)
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


//    override fun onItemSelected2(item: Question) {
//    }
//

}