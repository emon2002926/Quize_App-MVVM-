package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class QuestionBankViewModel @Inject constructor(private val questionBankRepository: QuestionBankRepository) :
    ViewModel() {

    private val pageNumber = 1

    private val _bcsYearName: MutableLiveData<Resource<MutableList<BcsYearName>>> =
        MutableLiveData()

    var bcsYearName: MutableLiveData<Resource<MutableList<BcsYearName>>> = _bcsYearName

    init {
        viewModelScope.launch { getYearName(5) }
    }

    suspend fun getYearName(apiNumber: Int) {
        _bcsYearName.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    questionBankRepository.getBcsYearName(apiNumber, pageNumber, PAGE_SIZE)
                _bcsYearName.postValue(handleSubjectNameResponse(response))
                bcsYearName = _bcsYearName

            } else {
                _bcsYearName.postValue(Resource.Error("No internet connection"))
                bcsYearName = _bcsYearName
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _bcsYearName.postValue(Resource.Error("Network Failure"))
                else -> _bcsYearName.postValue(Resource.Error("Conversion Error "))
            }
        }

    }

    private fun handleSubjectNameResponse(response: Response<MutableList<BcsYearName>>): Resource<MutableList<BcsYearName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    private fun hasInternetConnection(): Boolean {

        return true
    }

}