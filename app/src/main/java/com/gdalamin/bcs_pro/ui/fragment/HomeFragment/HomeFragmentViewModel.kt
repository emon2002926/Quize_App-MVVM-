package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalExamInfoRepository
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val examRepository: ExamRepository,
    private val examInfoRepository: LocalExamInfoRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    
    private val _liveExamInfo: MutableLiveData<DataState<MutableList<LiveExam>>> = MutableLiveData()
    val liveExamInfo: LiveData<DataState<MutableList<LiveExam>>> = _liveExamInfo
    private val pageNumber = 1
    
    fun getExamInfo(apiNumber: Int) {
        viewModelScope.launch {
            if (examInfoRepository.isDatabaseEmpty()) {
                _liveExamInfo.postValue(DataState.Loading())
                try {
                    val response = examRepository.getExamInfo(apiNumber, pageNumber, PAGE_SIZE)
                    val result = handleLiveExamResponse(response)
                    _liveExamInfo.postValue(result)
                    
                    if (result is DataState.Success) {
                        saveExamsToDatabase(result.data)
                    }
                    
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _liveExamInfo.postValue(
                            DataState.Error(
                                CHECK_INTERNET_CONNECTION_MESSAGE
                            )
                        )
                    }
                }
            } else {
                _liveExamInfo.postValue(
                    DataState.Success(
                        examInfoRepository.getAllExamsNonLive().toMutableList()
                    )
                )
            }
        }
    }
    
    private fun handleLiveExamResponse(response: Response<MutableList<LiveExam>>): DataState<MutableList<LiveExam>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return DataState.Success(it)
            }
        }
        return DataState.Error(response.message())
    }
    
    private suspend fun saveExamsToDatabase(exams: MutableList<LiveExam>?) {
        exams?.let {
            examInfoRepository.insertAll(it)
        }
    }
    
    fun deleteAllExamInfo() {
        viewModelScope.launch {
            examInfoRepository.deleteAllExamInfo()
        }
    }
    
    fun updateDatabase() {
        viewModelScope.launch {
            deleteAllExamInfo()
            delay(100) // Optional delay to ensure data is deleted before fetching new data
            getExamInfo(apiNumber = 2)
        }
    }
    
    
    fun clearDatabaseIfNeededTime() {
        val lastClearedTime = sharedPreferences.getLong("last_cleared_time", 0)
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastClearedTime >= 60 * 60 * 1000) { // 10 minutes in milliseconds
            updateDatabase()
            sharedPreferences.edit().putLong("last_cleared_time", currentTime).apply()
        }
    }
    
}