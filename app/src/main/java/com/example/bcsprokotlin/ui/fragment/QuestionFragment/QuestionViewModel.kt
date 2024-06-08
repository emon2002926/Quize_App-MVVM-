package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.repository.Repository
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


//    private fun handleExamQuestionResponse(response: Response<MutableList<Question>>): Resource<MutableList<Question>> {
//        if (response.isSuccessful) {
//            response.body()?.let {
//                return Resource.Success(it)
//            }
//        }
//        return Resource.Error(response.message())
//    }


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


}