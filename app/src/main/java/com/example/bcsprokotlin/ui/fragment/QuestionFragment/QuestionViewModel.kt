package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.model.ExamResult
import com.example.bcsprokotlin.model.OverallResult
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.util.Constants
import com.example.bcsprokotlin.util.Constants.Companion.PAGE_SIZE
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _questions: MutableLiveData<Resource<MutableList<Question>>> = MutableLiveData()
    val questions: LiveData<Resource<MutableList<Question>>> = _questions
    private val apiNumber = 1
    val pageNumber = 1

    // Track if data is already loaded
    private var isDataLoaded = false

    suspend fun getQuestion() {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val questionResponse = repository.getQuestion(apiNumber, pageNumber, PAGE_SIZE)
                    _questions.postValue(handleQuestionResponse(questionResponse))
                    isDataLoaded = true
                } else {
                    _questions.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                handleThrowable(t)
            }
        }
    }

    // Similar logic for other methods...
    suspend fun getPreviousYearQuestions(totalQuestion: Int, batchName: String) {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val questionResponse =
                        repository.getPreviousYearQuestion(9, pageNumber, totalQuestion, batchName)
                    _questions.postValue(handleQuestionResponse(questionResponse))
                    isDataLoaded = true
                } else {
                    _questions.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                handleThrowable(t)
            }
        }
    }

    suspend fun getExamQuestions(totalQuestion: Int) {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val questionResponse = repository.getExamQuestion(totalQuestion)
                    _questions.postValue(handleQuestionResponse(questionResponse))
                    isDataLoaded = true
                } else {
                    _questions.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                handleThrowable(t)
            }
        }
    }

    suspend fun getSubjectExamQuestions(subjectName: String, totalQuestion: Int) {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val questionResponse =
                        repository.getSubjectBasedExamQuestion(subjectName, totalQuestion)
                    _questions.postValue(handleQuestionResponse(questionResponse))
                    isDataLoaded = true
                } else {
                    _questions.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                handleThrowable(t)
            }
        }
    }

    private fun handleThrowable(t: Throwable) {
        _questions.postValue(
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        )
    }

    private fun handleQuestionResponse(response: Response<MutableList<Question>>): Resource<MutableList<Question>> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("No data")
        } else {
            Resource.Error(response.message())
        }
    }


    private fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication<BcsApplication>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            val activeNetwork = connectivityManager.activeNetwork?:return false
//            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
//
//            return when{
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->true
//                else->false
//            }
//        }else{
//            connectivityManager.activeNetworkInfo?.run {
//                return when(type){
//                    ConnectivityManager.TYPE_WIFI ->true
//                    ConnectivityManager.TYPE_MOBILE ->true
//                    ConnectivityManager.TYPE_ETHERNET ->true
//                    else->false
//                }
//
//            }
//        }
        return true
    }


    private val _overallResult = MutableLiveData<OverallResult>()
    val overallResult: LiveData<OverallResult> = _overallResult

    private val _results = MutableLiveData<List<ExamResult>>()
    val results: LiveData<List<ExamResult>> = _results


    fun submitAnswer(examType: String, questionLists: List<Question>) {
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


//    private fun handleExamQuestionResponse(response: Response<MutableList<Question>>): Resource<MutableList<Question>> {
//        if (response.isSuccessful) {
//            response.body()?.let {
//                return Resource.Success(it)
//            }
//        }
//        return Resource.Error(response.message())
//    }


}