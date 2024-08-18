package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalQuestionBankRepository
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionBankRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuestionBankViewModel @Inject constructor(
    private val questionBankRepository: QuestionBankRepository,
    private val localQuestionBankRepository: LocalQuestionBankRepository
) : ViewModel() {


    private val _bcsYearName: MutableLiveData<Resource<MutableList<BcsYearName>>> =
        MutableLiveData()
    val bcsYearName: LiveData<Resource<MutableList<BcsYearName>>> = _bcsYearName

    private val pageNumber = 1

    fun getBcsYearName(apiNumber: Int) {
        viewModelScope.launch {
            if (localQuestionBankRepository.isDatabaseEmpty()) {
                _bcsYearName.postValue(Resource.Loading())
                try {
                    val response = questionBankRepository.getBcsYearName(
                        apiNumber, pageNumber,
                        PAGE_SIZE
                    )
                    val result = handleResponseApi(response)
                    _bcsYearName.postValue(handleResponseApi(response))

                    if (result is Resource.Success) {
                        saveSubjectNameToDatabase(result.data)
                    }

                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _bcsYearName.postValue(Resource.Error("Network Failure"))
                        else -> _bcsYearName.postValue(Resource.Error("Conversion Error"))
                    }
                }
            } else {
                _bcsYearName.postValue(
                    Resource.Success(
                        localQuestionBankRepository.getAllBcsYearNameNonLive().toMutableList()
                    )
                )
            }
        }
    }

    private fun handleResponseApi(response: Response<MutableList<BcsYearName>>): Resource<MutableList<BcsYearName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun saveSubjectNameToDatabase(exams: MutableList<BcsYearName>?) {
        exams?.let {
            localQuestionBankRepository.insertAll(it)
        }
    }


    fun getBcsYearNameDatabase(): LiveData<List<BcsYearName>> {
        return localQuestionBankRepository.getAllBcsYearName()
    }

}