package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.model.ExamInfoTest
import com.gdalamin.bcs_pro.data.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

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

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> get() = _timeLeft

    private val _isTimerFinished = MutableLiveData<Boolean>()
    val isTimerFinished: LiveData<Boolean> get() = _isTimerFinished

    private var countDownTimer: CountDownTimer? = null

    private var isTimerStarted = false

    fun startCountDown(maxTimerSeconds: Int) {
        _isTimerFinished.value = false  // Reset the finish state
        if (!isTimerStarted) {
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
//                _timeLeft.value = GeneralUtils.convertEnglishToBangla(generateTime)
                    _timeLeft.value = generateTime
                }

                override fun onFinish() {
                    _timeLeft.value = "00:00:00"
                    _isTimerFinished.value = true  // Set the finish state to true
                }
            }
            countDownTimer?.start()
            isTimerStarted = true
        }

    }


}
