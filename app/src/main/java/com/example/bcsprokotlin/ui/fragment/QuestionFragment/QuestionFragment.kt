package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.util.Log
import android.view.LayoutInflater
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
import com.example.bcsprokotlin.databinding.SubmitAnswerOptionBinding
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate),
    QuestionAdapter.OnItemSelectedListener {
    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private var mBooleanValue = false

    private lateinit var questionList: Question
    private var questionLists = ArrayList<Question>()

    override fun loadUi() {

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
//        setupUI()
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
            "normalExam" -> {
                tvTitle.text = data.title
//                changeUiForExamFragment("time", "normalExam")
                setupFab("normalExam")
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "liveModelTest" -> {
//                changeUiForExamFragment("time", "liveModelTest")
                setupFab("normalExam")
                tvTitle.text = data.title
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "questionBank" -> {
                tvTitle.text = data.batchOrSubjectName
                setupFab("normalQuestion")
                questionAdapter.changeUiForExam("normalQuestion")
                viewModel.getPreviousYearQuestions(200, data.batchOrSubjectName)
            }

            "subjectBasedExam" -> {
                tvTitle.text = data.title
//                changeUiForExamFragment("time", "subjectBasedExam")
                setupFab("normalExam")
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
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

    fun changeUiForExamFragment(time: String, examType: String) = binding.apply {

        when (examType) {
            "normalExam", "subjectBasedExam", "liveModelTest" -> {
                setupFab("normalExam")
            }

//            "subjectBasedQuestions", "questionBank" -> {
//                setupFab("normalQuestion")
//            }

            else -> {
                setupFab("normalQuestion")
            }

        }
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
        binding.btnShowAnswer.setOnClickListener { submitAnswer() }

    }

    fun submitAnswer() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))

        var answerdQuestions = 0
        for (i in 0..questionLists.size - 1) {
            if (questionLists.get(i).userSelectedAnswer != 0) {
                answerdQuestions++
            }
        }

        logger("$answerdQuestions")
        bindingExamOption.apply {

        }


        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()


    }

    fun logger(message: String) = Log.d("QuestionFragmentLog", message)


    fun sectionSizeSelector(examType: String): IntArray? {
        var sectionSize: IntArray? = null
        if (examType.isNotEmpty()) {

            when (examType) {
                "200QuestionExam" -> sectionSize =
                    intArrayOf(20, 30, 35, 10, 10, 15, 35, 15, 15, 15)

                "100QuestionExam" -> sectionSize = intArrayOf(10, 15, 18, 5, 5, 7, 17, 8, 7, 8)
                "50QuestionExam" -> sectionSize = intArrayOf(5, 7, 9, 3, 3, 4, 8, 4, 3, 4)
            }

        }
        return sectionSize
    }
}