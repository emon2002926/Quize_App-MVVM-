package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import retrofit2.Response
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(private val repository: ExamRepository) : ViewModel() {

    private val _questions: MutableLiveData<Resource<MutableList<Question>>> = MutableLiveData()
    val questions: LiveData<Resource<MutableList<Question>>> = _questions
    var pageNumber = 1

    private var isDataLoaded = false

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

    suspend fun getExamQuestionsTest(
        numIA: Int,
        numBA: Int,
        numBLL: Int,
        numMVG: Int,
        numGEDM: Int,
        numML: Int,
        numELL: Int,
        numMA: Int,
        numGS: Int,
        numICT: Int
    ) {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val questionResponse = repository.getExamQuestionsTest(
                        numIA,
                        numBA,
                        numBLL,
                        numMVG,
                        numGEDM,
                        numML,
                        numELL,
                        numMA,
                        numGS,
                        numICT,
                    )
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

    private fun handleQuestionResponse(response: Response<MutableList<Question>>): Resource<MutableList<Question>> {
        return if (response.isSuccessful) {

            response.body()?.let {
                pageNumber++

                Resource.Success(it)

            } ?: Resource.Error("No data")
        } else {
            Resource.Error(response.message())
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


    private fun hasInternetConnection(): Boolean {
        return true
    }

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> get() = _timeLeft

    private val _isTimerFinished = MutableLiveData<Boolean>()
    val isTimerFinished: LiveData<Boolean> get() = _isTimerFinished

    private var countDownTimer: CountDownTimer? = null


    fun startCountDown(maxTimerSeconds: Int) {
        _isTimerFinished.value = false  // Reset the finish state

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
                _timeLeft.value = GeneralUtils.convertEnglishToBangla(generateTime)
            }

            override fun onFinish() {
                _timeLeft.value = "00:00:00"
                _isTimerFinished.value = true  // Set the finish state to true
            }
        }
        countDownTimer?.start()
    }


    /*
suspend fun getQuestion(pageNumber: Int) {
    if (!isDataLoaded) {
        _questions.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val questionResponse = repository.getQuestion(
                    apiNumber = apiNumber,
                    pageNumber = pageNumber,
                    limit = PAGE_SIZE
                )
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
                    repository.getPreviousYearQuestion(
                        apiNumber = 9,
                        pageNumber = pageNumber,
                        limit = totalQuestion,
                        batch = batchName
                    )
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


 */


}