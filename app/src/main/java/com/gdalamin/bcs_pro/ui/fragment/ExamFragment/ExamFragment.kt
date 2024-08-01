package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.ExamResult
import com.gdalamin.bcs_pro.data.model.OverallResult
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentExamBinding
import com.gdalamin.bcs_pro.databinding.ResultViewBinding
import com.gdalamin.bcs_pro.databinding.SubmitAnswerOptionBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.QuestionAdapter
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.ResultAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.convertEnglishToBangla
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.logger
import com.gdalamin.bcs_pro.ui.utilities.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>(FragmentExamBinding::inflate),
    QuestionAdapter.OnItemSelectedListener {

    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ExamViewModel by viewModels()
    private val resultViewmodel: ResultViewModel by viewModels()

    private var examType = ""
    private var answeredQuestions = 0
    private var totalQuestion = 0
    private var stringQuestion = ""
    private var isResultSubmitted = false
    private var countDownTimer: CountDownTimer? = null

    private var individualSubResult: List<ExamResult> = emptyList()

    private lateinit var sharedPreferences: SharedPreferences

    private val PREFS_NAME = "results_for_statistics"


    override fun loadUi() {

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding.apply {
            fabShowResult.setOnClickListener {
                setUpFabIcon()
            }
            backButton.setOnClickListener { findNavController().navigateUp() }
        }
        observeSharedData()
        observeQuestions()
        setupRecyclerView()
        observeResultViewModel()
    }


    private fun setUpFabIcon() = binding.apply {
        if (isResultSubmitted) {
            showResultView()
        } else {
            submitAnswer()
        }
    }

    private fun timeObserver(time: Int) = viewModel.apply {
        timeLeft.observe(viewLifecycleOwner) {

            binding.apply {
                tvTimer.visibility = View.VISIBLE
                tvTimer.text = "${it}"
            }
        }
        isTimerFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                showResultView()
            }
        }
        startCountDown(time)
    }

    private fun observeResultViewModel() = with(resultViewmodel) {
        overallResult.observe(viewLifecycleOwner) {
            stringQuestion = it.answeredQuestions.toString()
            answeredQuestions = it.answeredQuestions
        }
        resultSubmission.observe(viewLifecycleOwner) {
            isResultSubmitted = it
        }
        individualSubResultVm.observe(viewLifecycleOwner) {
            individualSubResult = it
        }
    }

    private fun saveResultForStatic(overallResult: OverallResult) {
        val overallNotAnswered: Int = totalQuestion - overallResult.answeredQuestions

        if (getInt("totalQuestions", 0) > 1) {

            val totalExam: Int = ((getInt("totalExam", 0)) + 1)
            val totalQuestion11: Int = ((getInt("totalQuestions", 0)) + totalQuestion)
            val overAllCorrectAnswer: Int =
                ((getInt("overAllCorrectAnswer", 0)) + overallResult.correctAnswers)
            val overAllWrongAnswer: Int =
                ((getInt("overAllWrongAnswer", 0)) + overallResult.wrongAnswers)
            val overAllNotAnswered: Int =
                ((getInt("overAllNotAnswered", 0)) + overallNotAnswered)

            saveInt("totalExam", totalExam)
            saveInt("totalQuestions", totalQuestion11)
            saveInt("overAllCorrectAnswer", overAllCorrectAnswer)
            saveInt("overAllWrongAnswer", overAllWrongAnswer)
            saveInt("overAllNotAnswered", overAllNotAnswered)

        } else {
            saveInt("totalExam", 1)
            saveInt("totalQuestions", totalQuestion)
            saveInt("overAllCorrectAnswer", overallResult.correctAnswers)
            saveInt("overAllWrongAnswer", overallResult.wrongAnswers)
            saveInt("overAllNotAnswered", overallNotAnswered)
        }

    }

    private fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
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
                timeObserver(data.time)
            }

            "liveModelTest" -> {
                tvTitle.text = data.title
                totalQuestion = data.totalQuestion
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getExamQuestions(data.totalQuestion)
                timeObserver(data.time)
            }

            "subjectBasedExam" -> {
                tvTitle.text = data.title
                totalQuestion = data.totalQuestion
                questionAdapter.changeUiForExam("examQuestion")
                viewModel.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
                timeObserver(data.time)

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
                    binding.fabShowResult.visibility = View.GONE
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvExamQuestion)
                    response.data?.let {
                        questionAdapter.submitList(it)
                    }
                    binding.fabShowResult.visibility = View.VISIBLE
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
        resultViewmodel.addQuestion(item)
        logger(answeredQuestions.toString())

        resultViewmodel.calculateResults(examType)
        resultViewmodel.overallResult.observe(viewLifecycleOwner) {
            answeredQuestions = it.answeredQuestions
        }
    }

    @SuppressLint("SetTextI18n")
    fun submitAnswer() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))

        bindingResult.tvDis.text =
            "আপনি ${convertEnglishToBangla(totalQuestion.toString())}" +
                    " প্রশ্নের মধ্যে ${
                        convertEnglishToBangla(
                            answeredQuestions.toString()
                        )
                    } টি প্রশ্নের উত্তর দিয়েছেন"

        bindingResult.apply {
            btnSubmit.setOnClickListener {
                if (answeredQuestions == 0) {
                    Toast.makeText(
                        context,
                        "Please answer Question to see result",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    resultViewmodel.overallResult.observe(viewLifecycleOwner) {
                        saveResultForStatic(it)
                    }
                    binding.fabShowResult.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24)
                    bottomSheetDialog.dismiss()
                    questionAdapter.showAnswer(true)
                    showResultView()
                }
            }
        }
        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }

    private fun showResultView() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = ResultViewBinding.inflate(LayoutInflater.from(context))

        resultViewmodel.setBooleanValue(true)
        val resultAdapter by lazy { ResultAdapter() }
        bindingResult.apply {
            resultViewmodel.overallResult.observe(viewLifecycleOwner) {
                tvAnsweredQuestion.text = "${it.answeredQuestions}"
                tvMarks.text = "${it.mark}"
                tvCorrectAnswer.text = "${it.correctAnswers}"
                tvWrongAnswer.text = "${it.wrongAnswers}"
            }
            resultAdapter.submitList(individualSubResult)
            rvResultView.adapter = resultAdapter
            rvResultView.layoutManager = LinearLayoutManager(context)
        }
        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }


}
