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
        viewModel.submitAnswer("50QuestionExam", questionLists)
        binding.btnShowAnswer.setOnClickListener { resultObserver() }

    }
    ////////////////////////////////////


    fun resultObserver() {
        viewModel.results.observe(viewLifecycleOwner) { result ->
            result.forEach { resultw ->
                logger(
                    "Subject: ${resultw.subjectName}, Mark: ${resultw.mark}" +
                            ", Correct: ${resultw.correctAnswer}, Wrong: ${resultw.wrongAnswer}" +
                            ", Answered: ${resultw.answeredQuestions}"
                )
            }
        }
    }

    /*
    // For Now
    val subjects = listOf(
        "internationalAffairs",
        "bangladeshAffairs",
        "bangla",
        "ethicsAndGooGovernance",
        "geography",
        "math",
        "english",
        "mentalAbility",
        "generalScience",
        "ict"
    )

    fun submitAnswer(examType: String) {
        val sectionSizes = sectionSizeSelector(examType) ?: return
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))

        var currentIndex = 0
        val results = mutableListOf<ExamResult>()

        sectionSizes.forEachIndexed { index, sectionSize ->
            var correctAnswers = 0
            var wrongAnswers = 0
            var answeredQuestions = 0

            for (i in 0 until sectionSize) {
                val question = questionLists.getOrNull(currentIndex)
                if (question != null) {
                    if (question.userSelectedAnswer != 0) {
                        answeredQuestions++
                        if (question.userSelectedAnswer == question.answer.toInt()) {
                            correctAnswers++
                        } else {
                            wrongAnswers++
                        }
                    }
                    currentIndex++
                }
            }

            val mark =
                correctAnswers * (100.0 / sectionSizes.sum()) // Adjust this formula as needed
            results.add(
                ExamResult(
                    subjectName = subjects.getOrNull(index) ?: "Unknown",
                    mark = mark,
                    correctAnswer = correctAnswers,
                    wrongAnswer = wrongAnswers,
                    answeredQuestions = answeredQuestions
                )
            )
        }

        // Log or display results
        results.forEach { result ->
            logger("Subject: ${result.subjectName}, Mark: ${result.mark}, Correct: ${result.correctAnswer}, Wrong: ${result.wrongAnswer}, Answered: ${result.answeredQuestions}")
        }

        bindingExamOption.apply {

        }
        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()

        // Additional UI handling for showing the result can go here
    }


     */


    //////////////////
    fun submitAnswer2() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))


        var answeredQuestions = 0
        var correctAnswers = 0
        var wrongAnswers = 0
        for (i in 0..questionLists.size - 1) {

            if (questionLists.get(i).userSelectedAnswer != 0) {
                answeredQuestions++
            }
            if (questionLists.get(i).userSelectedAnswer == questionLists.get(i).answer.toInt()) {
                correctAnswers++
            } else {
                wrongAnswers++
            }
        }
        logger(
            "answeredQuestions:$answeredQuestions \n correctAnswers:$correctAnswers  \n " +
                    "wrongAnswers:$wrongAnswers"
        )


        /*
        else {
            val sectionSize = sectionSizeSelector("50QuestionExam")
            val startIndex = 0

            var answeredQuestions = 0
            var correctAnswers = 0
            var wrongAnswers = 0
            if (sectionSize != null) {
                for (i in 0..sectionSize.size) {

                    val endIndex = Math.min(startIndex + sectionSize[i], questionLists.size)
                    val sectionQuestions: List<Question> =
                        questionLists.subList(startIndex, endIndex)


                    for (i in 0..sectionQuestions.size - 1) {

                        if (sectionQuestions.get(i).userSelectedAnswer != 0) {
                            answeredQuestions++
                        }

                        if (sectionQuestions.get(i).userSelectedAnswer == sectionQuestions.get(i).answer.toInt()) {
                            correctAnswers++
                        } else {
                            wrongAnswers++

                        }

                    }

                    when (i) {
                        0 -> logger("Inrenational Affairs \n Correct Answer:$correctAnswers Wrong Answer:$wrongAnswers")
                        1 -> logger("Bangladesh Affairs \n Correct Answer:$correctAnswers Wrong Answer:$wrongAnswers")
                        2 -> {}
                        3 -> {}
                        4 -> {}
                        5 -> {}
                        6 -> {}
                        7 -> {}
                        8 -> {}
                        9 -> {}

                    }


                }


            }
        }


         */

        ///

        bindingExamOption.apply {

        }


        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()


    }

    fun logger(message: String) = Log.d("QuestionFragmentLog", message)

    fun sectionSizeSelector(examType: String): IntArray? {
        return when (examType) {
            "200QuestionExam" -> intArrayOf(20, 30, 35, 10, 10, 15, 35, 15, 15, 15)
            "100QuestionExam" -> intArrayOf(10, 15, 18, 5, 5, 7, 17, 8, 7, 8)
            "50QuestionExam" -> intArrayOf(5, 7, 9, 3, 3, 4, 8, 4, 3, 4)
            else -> null
        }
    }
    /*
    //Old
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

     */
}