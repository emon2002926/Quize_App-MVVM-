package com.example.bcsprokotlin.ui.fragment.HomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcsprokotlin.model.LiveExam
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.util.Constants.Companion.PAGE_SIZE
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _liveExamInfo: MutableLiveData<Resource<MutableList<LiveExam>>> = MutableLiveData()
    var liveExamInfo: LiveData<Resource<MutableList<LiveExam>>> = _liveExamInfo

    private val pageNumber = 1

    init {
        viewModelScope.launch {
            getLiveExamInfo(2)
        }
    }

    suspend fun getLiveExamInfo(apiNumber: Int) {
        _liveExamInfo.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getExamInfo(apiNumber, pageNumber, PAGE_SIZE)
                _liveExamInfo.postValue(handleSubjectNameResponse(response))
//                liveExamInfo = _liveExamInfo

            } else {
                _liveExamInfo.postValue(Resource.Error("No internet connection"))
//                liveExamInfo = _liveExamInfo
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _liveExamInfo.postValue(Resource.Error("Network Failure"))
                else -> _liveExamInfo.postValue(Resource.Error("Conversion Error "))
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

    fun hasInternetConnection(): Boolean {
        return true
    }
}