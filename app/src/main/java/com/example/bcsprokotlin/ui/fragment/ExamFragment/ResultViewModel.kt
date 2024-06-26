package com.example.bcsprokotlin.ui.fragment.ExamFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.model.ExamResult
import com.example.bcsprokotlin.model.OverallResult
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.util.Constants

class ResultViewModel : ViewModel() {
    private val _questionLists = MutableLiveData<MutableList<Question>>(mutableListOf())
    val questionLists: LiveData<MutableList<Question>> = _questionLists

    private val _overallResult = MutableLiveData<OverallResult>()
    val overallResult: LiveData<OverallResult> = _overallResult

    private val _results = MutableLiveData<List<ExamResult>>()
    val results: LiveData<List<ExamResult>> = _results

    fun addQuestion(item: Question) {
        _questionLists.value?.add(item)
        _questionLists.value = _questionLists.value // Trigger observer
    }

    fun calculateResults(examType: String) {
        val questionLists = _questionLists.value ?: return
        val sectionSizes = sectionSizeSelector(examType) ?: return

        var currentIndex = 0
        val results = mutableListOf<ExamResult>()
        var totalCorrectAnswers = 0
        var totalWrongAnswers = 0
        var totalAnsweredQuestions = 0
        var totalMark = 0.0

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

            totalCorrectAnswers += correctAnswers
            totalWrongAnswers += wrongAnswers
            totalAnsweredQuestions += answeredQuestions

            val mark = (correctAnswers - ((wrongAnswers / 2).toDouble()))

            results.add(
                ExamResult(
                    subjectName = Constants.subjectsName.getOrNull(index) ?: "Unknown",
                    mark = mark,
                    correctAnswer = correctAnswers,
                    wrongAnswer = wrongAnswers,
                    answeredQuestions = answeredQuestions
                )
            )
        }

        totalMark = (totalCorrectAnswers - ((totalWrongAnswers / 2).toDouble()))

        _overallResult.value = OverallResult(
            answeredQuestions = totalAnsweredQuestions,
            correctAnswers = totalCorrectAnswers,
            wrongAnswers = totalWrongAnswers,
            mark = totalMark
        )
        _results.value = results
    }

    private fun sectionSizeSelector(examType: String): IntArray? {
        return when (examType) {
            "200QuestionExam" -> intArrayOf(20, 30, 35, 10, 10, 15, 35, 15, 15, 15)
            "100QuestionExam" -> intArrayOf(10, 15, 18, 5, 5, 7, 17, 8, 7, 8)
            "50QuestionExam" -> intArrayOf(5, 7, 9, 3, 3, 4, 8, 4, 3, 4)
            else -> null
        }
    }
}
