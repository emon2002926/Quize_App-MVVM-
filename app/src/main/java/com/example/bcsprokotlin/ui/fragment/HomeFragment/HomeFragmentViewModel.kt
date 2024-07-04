package com.example.bcsprokotlin.ui.fragment.HomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcsprokotlin.model.LiveExam
import com.example.bcsprokotlin.repository.ExamInfoRepository
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.util.Constants.Companion.PAGE_SIZE
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val examInfoRepository: ExamInfoRepository
) : ViewModel() {

    private val _liveExamInfo: MutableLiveData<Resource<MutableList<LiveExam>>> = MutableLiveData()
    val liveExamInfo: LiveData<Resource<MutableList<LiveExam>>> = _liveExamInfo
    private val pageNumber = 1

    fun getExamInfo(apiNumber: Int) {
        viewModelScope.launch {
            if (examInfoRepository.isDatabaseEmpty()) {
                _liveExamInfo.postValue(Resource.Loading())
                try {
                    if (hasInternetConnection()) {
                        val response = repository.getExamInfo(apiNumber, pageNumber, PAGE_SIZE)
                        val result = handleSubjectNameResponse(response)
                        _liveExamInfo.postValue(result)

                        if (result is Resource.Success) {
                            saveExamsToDatabase(result.data)
                        }
                    } else {
                        _liveExamInfo.postValue(Resource.Error("No internet connection"))
                    }
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _liveExamInfo.postValue(Resource.Error("Network Failure"))
                        else -> _liveExamInfo.postValue(Resource.Error("Conversion Error"))
                    }
                }
            } else {
                _liveExamInfo.postValue(
                    Resource.Success(
                        examInfoRepository.getAllExamsNonLive().toMutableList()
                    )
                )
            }
        }
    }

    private fun handleSubjectNameResponse(response: Response<MutableList<LiveExam>>): Resource<MutableList<LiveExam>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun saveExamsToDatabase(exams: MutableList<LiveExam>?) {
        exams?.let {
            examInfoRepository.insertAll(it)
        }
    }

    fun getExamsFromDatabase(): LiveData<List<LiveExam>> {
        return examInfoRepository.getAllExams()
    }

    private fun hasInternetConnection(): Boolean {
        return true
    }
}