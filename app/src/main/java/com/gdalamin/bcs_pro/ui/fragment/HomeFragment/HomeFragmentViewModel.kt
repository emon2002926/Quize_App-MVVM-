package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.ExamInfoRepository
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.data.repository.Repository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val examRepository: ExamRepository,
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
                        val response = examRepository.getExamInfo(apiNumber, pageNumber, PAGE_SIZE)
                        val result = handleLiveExamResponse(response)
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

    private fun handleLiveExamResponse(response: Response<MutableList<LiveExam>>): Resource<MutableList<LiveExam>> {
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