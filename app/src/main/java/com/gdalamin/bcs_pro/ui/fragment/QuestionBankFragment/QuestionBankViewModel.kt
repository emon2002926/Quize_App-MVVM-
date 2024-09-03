package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalQuestionBankRepository
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionBankRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuestionBankViewModel @Inject constructor(
    private val questionBankRepository: QuestionBankRepository,
    private val localQuestionBankRepository: LocalQuestionBankRepository,
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
                    val response = questionBankRepository.getBcsYearName(
                        apiNumber, pageNumber,
                        PAGE_SIZE
                    )
                    val result = handleResponseApi(response)
                    _bcsYearName.postValue(handleResponseApi(response))
                    
                    if (result is DataState.Success) {
                        saveSubjectNameToDatabase(result.data)
                    }
                    
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _bcsYearName.postValue(
                            DataState.Error(
                                CHECK_INTERNET_CONNECTION_MESSAGE
                            )
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
    
    
    private suspend fun saveSubjectNameToDatabase(exams: MutableList<BcsYearName>?) {
        exams?.let {
            localQuestionBankRepository.insertAll(it)
        }
    }
    
    
    fun getBcsYearNameDatabase(): LiveData<List<BcsYearName>> {
        return localQuestionBankRepository.getAllBcsYearName()
    }
    
    
    /* New Code
    private val _bcsYearName: MutableLiveData<DataState<MutableList<BcsYearName>>> =
        MutableLiveData()
    val bcsYearName: LiveData<DataState<MutableList<BcsYearName>>> = _bcsYearName

    private val pageNumber = 1
    private val PAGE_SIZE = 50 // Adjust according to your pagination needs

    fun getBcsYearName(apiNumber: Int, dd: String) {
        viewModelScope.launch {
            when (dd) {
                "1" -> {
                    _bcsYearName.postValue(DataState.Loading())
                    getBcsYearNameNetwork(apiNumber)
                }

                "2" -> {
                    localQuestionBankRepository.getAllBcsYearName().observeForever { exams ->
                        _bcsYearName.postValue(DataState.Success(exams.toMutableList()))
                    }
                }
            }
//            if (networkAvailable()) {
//                _bcsYearName.postValue(DataState.Loading())
//                getBcsYearNameNetwork(apiNumber)
//            } else {
//                localQuestionBankRepository.getAllBcsYearName().observeForever { exams ->
//                    _bcsYearName.postValue(DataState.Success(exams.toMutableList()))
//                }
//            }
        }
    }

    private fun networkAvailable(): Boolean {
        // Implement your network check logic here
        return false
    }

    private fun getBcsYearNameNetwork(apiNumber: Int) = viewModelScope.launch {
        try {
            val response = questionBankRepository.getBcsYearName(apiNumber, pageNumber, PAGE_SIZE)
            val result = handleResponseApi(response)
            _bcsYearName.postValue(result)
            if (result is DataState.Success) {
                deleteAllBcsYearName() // Delete existing data
                saveSubjectNameToDatabase(result.data)
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _bcsYearName.postValue(DataState.Error("Network Failure"))
                else -> _bcsYearName.postValue(DataState.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponseApi(response: Response<MutableList<BcsYearName>>): DataState<MutableList<BcsYearName>> {
        return if (response.isSuccessful) {
            response.body()?.let {
                DataState.Success(it)
            } ?: DataState.Error("No Data")
        } else {
            DataState.Error(response.message())
        }
    }

    private suspend fun saveSubjectNameToDatabase(exams: MutableList<BcsYearName>?) {
        exams?.let {
            localQuestionBankRepository.insertAll(it)
        }
    }

    private suspend fun deleteAllBcsYearName() {
        localQuestionBankRepository.deleteAllBcsYearName()
    }
    
     */
}