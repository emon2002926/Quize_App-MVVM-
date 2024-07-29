package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.gdalamin.bcs_pro.model.SharedData
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
    private val questionAdapterPaging by lazy { QuestionAdapterPaging(this) }
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

        // Set up the RecyclerView
//        binding.rvQuestion.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = questionAdapterPaging
//        }

        // Observe the data and submit it to the adapter
//        lifecycleScope.launchWhenStarted {
//            testViewModel.questions.collectLatest { pagingData ->
//                questionAdapterPaging.submitData(pagingData)
//            }
//        }

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
//                old
                tvTitle.text = data.title
                setupFab()
//                questionAdapter.changeUiForExam("normalQuestion")
//                viewModel.getQuestion(1)

                testViewModel.getQuestions(1)
                observePagingQuestions()
            }
        }
    }

    private fun observePagingQuestions() = binding.apply {

        setupRecyclerViewPaging()

        lifecycleScope.launchWhenStarted {
            testViewModel.questions.collectLatest { pagingData ->
                questionAdapterPaging.submitData(pagingData)
            }
        }

//      Add LoadStateListener to handle loading states
        questionAdapterPaging.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    // Initial load - show shimmer
                    shimmerLayout.startShimmer()
                    shimmerLayout.visibility = View.VISIBLE
                    rvQuestion.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    // Data loaded - hide shimmer
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    rvQuestion.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }

                is LoadState.Error -> {
                    // Error loading data - hide shimmer and show retry button
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    rvQuestion.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error loading data", Toast.LENGTH_LONG).show()
                }
            }

            // Handle the loading state for paginated data
            if (loadState.append is LoadState.Loading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
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

    private fun setupRecyclerViewPaging() = binding.rvQuestion.apply {
        adapter = questionAdapterPaging
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
//        observeQuestions()
    }

    override fun onDisconnected() {
    }


}