package com.example.bcsprokotlin.ui.fragment.ExamFragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.adapter.ResultAdapter
import com.example.bcsprokotlin.databinding.FragmentExamBinding
import com.example.bcsprokotlin.databinding.ResultViewBinding
import com.example.bcsprokotlin.databinding.SubmitAnswerOptionBinding
import com.example.bcsprokotlin.model.ExamResult
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.ui.fragment.QuestionFragment.QuestionViewModel
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>(FragmentExamBinding::inflate),
    QuestionAdapter.OnItemSelectedListener {


    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private var mBooleanValue = false

    private var examType = ""

    private var questionLists = ArrayList<Question>()

    private var resultList = ArrayList<ExamResult>()
    private var answeredQuestions = 0

    private var totalQuestion = 0

    override fun loadUi() {

        binding.btnShowAnswer.setOnClickListener {
            submitAnswer("null")
        }
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
                totalQuestion = data.totalQuestion
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getExamQuestions(data.totalQuestion)
                examType = data.questionType

            }

            "liveModelTest" -> {
                tvTitle.text = data.title
                totalQuestion = data.totalQuestion
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getExamQuestions(data.totalQuestion)
            }

            "subjectBasedExam" -> {
                tvTitle.text = data.title
                totalQuestion = data.totalQuestion
                questionAdapter.changeUiForExam("examQuestion")
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
                        binding.rvExamQuestion
                    )
                    binding.btnShowAnswer.visibility = View.GONE
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvExamQuestion)
                    response.data?.let {
                        binding.btnShowAnswer.setOnClickListener { submitAnswer("null") }
                        questionAdapter.submitList(it)
                    }
                    binding.btnShowAnswer.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvExamQuestion)
                    Toast.makeText(activity, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupRecyclerView() = binding.rvExamQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)
    }


    override fun onItemSelected(item: Question) {
        questionLists.add(item)
        binding.btnShowAnswer.setOnClickListener { submitAnswer("notNull") }
        viewModel.results.observe(viewLifecycleOwner) {
            resultList.addAll(it)
            it.forEach {
                answeredQuestions = it.answeredQuestions
            }
        }
        answeredQuestions++
    }

    @SuppressLint("SetTextI18n")
    fun submitAnswer(action: String) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))

        when (action) {
            "null" -> bindingResult.tvDis.text =
                "আপনি ${GeneralUtils.convertEnglishToBengaliNumber(totalQuestion.toString())}" +
                        " প্রশ্নের মধ্যে ${GeneralUtils.convertEnglishToBengaliNumber("0")} টি প্রশ্নের উত্তর দিয়েছেন"

            "notNull" -> bindingResult.tvDis.text =
                "আপনি ${GeneralUtils.convertEnglishToBengaliNumber(totalQuestion.toString())}" +
                        " প্রশ্নের মধ্যে ${
                            GeneralUtils.convertEnglishToBengaliNumber(
                                answeredQuestions.toString()
                            )
                        } টি প্রশ্নের উত্তর দিয়েছেন"
        }

        bindingResult.apply {
            btnSubmit.setOnClickListener {
                when (action) {
                    "null" -> {
                        Toast.makeText(
                            context,
                            "Please answer Question to see result",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    "notNull" -> {
                        bottomSheetDialog.hide()
                        questionAdapter.showAnswer(true)
                        viewModel.submitAnswer("50QuestionExam", questionLists)
                        viewModel.results.observe(viewLifecycleOwner) {
                            resultView(it)
                        }
                    }
                }
            }
        }

        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }

    private fun resultView(examResult: List<ExamResult>) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = ResultViewBinding.inflate(LayoutInflater.from(context))

        val resultAdapter by lazy { ResultAdapter() }
        bindingResult.apply {
            viewModel.overallResult.observe(viewLifecycleOwner) {
                tvAnsweredQuestion.text = "${it.answeredQuestions}"
                tvMarks.text = "${it.mark}"
                tvCorrectAnswer.text = "${it.correctAnswers}"
                tvWrongAnswer.text = "${it.wrongAnswers}"
            }
            resultAdapter.submitList(examResult)
            rvResultView.adapter = resultAdapter
            rvResultView.layoutManager = LinearLayoutManager(context)
        }
        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }

    fun logger(message: String) = Log.d("QuestionFragmentLog", message)

}