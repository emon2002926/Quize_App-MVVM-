package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.model.ExamInfoTest
import com.gdalamin.bcs_pro.data.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {

    private val _isResultSubmitted = MutableLiveData<Boolean>()
    val resultSubmission: LiveData<Boolean> get() = _isResultSubmitted


    private val _overallResult = MutableLiveData<ExamInfoTest>()
    var overallResult: LiveData<ExamInfoTest> = _overallResult

    fun setBooleanValue(value: Boolean) {
        _isResultSubmitted.value = value
    }

    // LiveData to hold the list of ExamResults
    private val _liveDataExamResults = MutableLiveData<List<ExamInfoTest>>()
    val liveDataExamResults: LiveData<List<ExamInfoTest>> get() = _liveDataExamResults

    private val subjectsName = mapOf(
        "IA" to "আন্তর্জাতিক বিষয়াবলি",
        "BA" to "বাংলাদেশ বিষয়াবলি",
        "GEDM" to "নৈতিকতা মুল্যবোধ ও সুশাসন",
        "BLL" to "বাংলা",
        "ELL" to "ইংরেজি",
        "ML" to "মানসিক দক্ষতা",
        "MVG" to "ভূগোল",
        "GS" to "সাধারণ বিজ্ঞান",
        "MA" to "গণিত",
        "ICT" to "কম্পিউটার ও তথ্য প্রযুক্তি"
    )

    private val examResultsMap = mutableMapOf<String, ExamInfoTest>()

    init {
        // Initialize the map with default ExamResult for each subject
        subjectsName.forEach { (subjectCode, subjectName) ->
            examResultsMap[subjectCode] = ExamInfoTest(
                subjectName = subjectName,
                totalSelectedAnswer = 0,
                totalCorrectAnswer = 0,
                totalWrongAnswer = 0,
                totalMark = 0.0

            )
        }
        _liveDataExamResults.value = examResultsMap.values.toList()
    }

    // Function to process each question and update the results

    var totalAnsweredQuestion = 0
    var totalCorrectAnswer = 0
    var totalWrongAnswer = 0
    var totalMark = 0.0

    fun processQuestion(question: Question) {
        var answerQuestion = 0
        var correctAnswer = 0
        var wrongAnswer = 0
        if (question.userSelectedAnswer != 0) {
            answerQuestion++
            if (question.userSelectedAnswer == question.answer.toInt()) {
                correctAnswer++
            } else {
                wrongAnswer++
            }
            totalAnsweredQuestion += answerQuestion
            totalCorrectAnswer += correctAnswer
            totalWrongAnswer += wrongAnswer
            totalMark = (totalCorrectAnswer - ((totalWrongAnswer / 2).toDouble()))

            _overallResult.value = ExamInfoTest(
                subjectName = "",
                totalSelectedAnswer = totalAnsweredQuestion,
                totalCorrectAnswer = totalCorrectAnswer,
                totalWrongAnswer = totalWrongAnswer,
                totalMark = totalMark
            )

        }

        viewModelScope.launch(Dispatchers.Default) {
            val subjectCode = question.subjects
            val examResult = examResultsMap[subjectCode] ?: return@launch

            if (question.userSelectedAnswer != 0) {
                examResult.totalSelectedAnswer++

                if (question.userSelectedAnswer == question.answer.toInt()) {
                    examResult.totalCorrectAnswer++
                } else {
                    examResult.totalWrongAnswer++
                }

                // Calculate the mark using the provided logic
                examResult.totalMark =
                    (examResult.totalCorrectAnswer - ((examResult.totalWrongAnswer / 2).toDouble()))
            }

            // Update LiveData with the latest results
            _liveDataExamResults.postValue(examResultsMap.values.toList())
        }
    }


}
