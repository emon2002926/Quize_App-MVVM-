package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalQuestionBankRepository
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionBankRepository
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuestionBankViewModel @Inject constructor(
    private val questionBankRepository: QuestionBankRepository,
    private val localQuestionBankRepository: LocalQuestionBankRepository,
    private val questionRepository: QuestionRepository,
) : ViewModel() {

    private val _bcsYearName: MutableLiveData<DataState<MutableList<BcsYearName>>> =
        MutableLiveData()
    val bcsYearName: LiveData<DataState<MutableList<BcsYearName>>> = _bcsYearName

    private val pageNumber = 1

    fun getBcsYearName(apiNumber: Int) {
        viewModelScope.launch {
            if (localQuestionBankRepository.isDatabaseEmpty()) {
                _bcsYearName.postValue(DataState.Loading())
                try {
                    val response =
                        questionBankRepository.getBcsYearName(apiNumber, pageNumber, PAGE_SIZE)
                    _bcsYearName.postValue(handleResponseApi(response))
                    response.apply {
                        if (isSuccessful) {
                            body()?.let {
                                _bcsYearName.postValue(DataState.Success(it))
                                saveBcsYearName(it)
                            }
                        }
                    }
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _bcsYearName.postValue(
                            DataState.Error(CHECK_INTERNET_CONNECTION_MESSAGE)
                        )
                    }
                }
            } else {
                _bcsYearName.postValue(
                    DataState.Success(
                        localQuestionBankRepository.getAllBcsYearNameNonLive().toMutableList()
                    )
                )
            }
        }
    }

    private fun handleResponseApi(response: Response<MutableList<BcsYearName>>): DataState<MutableList<BcsYearName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return DataState.Success(it)
            }
        }
        return DataState.Error(response.message())
    }

    private suspend fun saveBcsYearName(exams: MutableList<BcsYearName>?) {
        exams?.let {
            localQuestionBankRepository.insertAll(it)
        }
    }

    fun getBcsYearNameDatabase(): LiveData<List<BcsYearName>> {
        return localQuestionBankRepository.getAllBcsYearName()
    }

    // Track currently downloading items
    private val _currentlyDownloading = MutableLiveData<MutableSet<Int>>(mutableSetOf())
    val currentlyDownloading: LiveData<MutableSet<Int>> = _currentlyDownloading

    private val _downloadComplete = MutableLiveData<Int>()
    val downloadComplete: LiveData<Int> get() = _downloadComplete

    private val _downloadError = MutableLiveData<Boolean?>()
    val downloadError: LiveData<Boolean?> get() = _downloadError

    private val downloadMutex = Mutex() // To ensure only one download at a time

    fun getYearQuestion(id: Int, batchName: String, totalQuestion: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadMutex.withLock {
                try {
                    val response =
                        questionRepository.forYearQuestionDownload(totalQuestion, batchName)
                    response.apply {
                        if (isSuccessful) {
                            body()?.let { questionList ->
                                saveQuestions(questionList)
                                updateIsQuestionSaved(id, true)
                                _downloadComplete.postValue(id)
                                _downloadError.postValue(false)
                            }
                        } else {
                            _downloadError.postValue(true)
                            Log.e(
                                "DownloadError",
                                "Download failed with response code: ${response.code()}"
                            )
                        }
                    }
                } catch (e: Throwable) {
                    _downloadError.postValue(true)
                    Log.e("DownloadError", "Error downloading questions: ${e.message}")
                }
            }
        }
    }

    fun saveQuestions(question: MutableList<Question>) {
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository.addQuestions(question)
        }
    }

    fun updateIsQuestionSaved(id: Int, isSaved: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            localQuestionBankRepository.updateIsQuestionSaved(id, isSaved)
        }
    }

    // Mark a download as started
    fun markDownloadStarted(id: Int) {
        _currentlyDownloading.value?.add(id)
        _currentlyDownloading.postValue(_currentlyDownloading.value)
    }

    // Mark a download as completed
    fun markDownloadCompleted(id: Int) {
        _currentlyDownloading.value?.remove(id)
        _currentlyDownloading.postValue(_currentlyDownloading.value)
    }

    fun resetDownloadError() {
        _downloadError.postValue(null)
    }
}
