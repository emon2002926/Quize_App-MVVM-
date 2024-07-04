package com.example.bcsprokotlin.ui.fragment.ExamFragment

import android.annotation.SuppressLint
import android.os.CountDownTimer
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
    private val resultViewmodel: ResultViewModel by viewModels()

    private var examType = ""
    private var answeredQuestions = 0
    private var totalQuestion = 0
    private var stringQuestion = ""
    private var isResultSubmitted = false
    private var countDownTimer: CountDownTimer? = null

    private var individualSubResult: List<ExamResult> = emptyList()

    override fun loadUi() {
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
                    binding.fabShowResult.visibility = View.GONE
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvExamQuestion)
                    response.data?.let {
                        questionAdapter.submitList(it)
                    }
                    binding.fabShowResult.visibility = View.VISIBLE
//                    countDownTimer(50)
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
            "আপনি ${GeneralUtils.convertEnglishToBengaliNumber(totalQuestion.toString())}" +
                    " প্রশ্নের মধ্যে ${
                        GeneralUtils.convertEnglishToBengaliNumber(
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


    /*
    private fun countDownTimer(maxTimerSeconds: Int) {
        countDownTimer = object : CountDownTimer(maxTimerSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val getMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

                val generateTime = String.format(
                    Locale.getDefault(), "%02d:%02d:%02d", getHour,
                    getMinutes - TimeUnit.HOURS.toMinutes(getHour),
                    getSecond - TimeUnit.MINUTES.toSeconds(getMinutes)
                )
                binding.tvTimer.visibility = View.VISIBLE
                binding.tvTimer.text = "${GeneralUtils.convertEnglishToBengaliNumber(generateTime)}"
            }

            override fun onFinish() {

                showResultView()
            }
        }
        (countDownTimer as CountDownTimer).start()
    }

     */

//    private fun startTimer(maxTimerSeconds: Int, textViewTimer: TextView) {
//        countDownTimer = object : CountDownTimer(maxTimerSeconds * 1000L, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
//                val getMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
//                val getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
//
//                val generateTime = String.format(
//                    Locale.getDefault(), "%02d:%02d:%02d", getHour,
//                    getMinutes - TimeUnit.HOURS.toMinutes(getHour),
//                    getSecond - TimeUnit.MINUTES.toSeconds(getMinutes)
//                )
//                textViewTimer.setText(convertToBengaliString(generateTime))
//            }
//
//            override fun onFinish() {
//                if (timerCallback != null) {
//                    timerCallback.onTimerFinish()
//                }
//            }
//        }
//        (countDownTimer as CountDownTimer).start()
//    }
//
//    fun stopTimer() {
//        if (countDownTimer != null) {
//            countDownTimer.cancel()
//            textViewTimer.setVisibility(View.GONE)
//        }
//    }


    fun logger(message: String) = Log.d("QuestionFragmentLog", message)
}
