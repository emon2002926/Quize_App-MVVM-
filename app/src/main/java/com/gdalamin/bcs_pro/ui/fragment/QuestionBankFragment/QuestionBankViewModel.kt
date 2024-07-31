package com.gdalamin.bcs_pro.ui.fragment.QuestionBankFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.repository.Repository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuestionBankViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
                val response = repository.getBcsYearName(apiNumber, pageNumber, PAGE_SIZE)
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