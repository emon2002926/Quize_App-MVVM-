package com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.model.SubjectName
import com.gdalamin.bcs_pro.repository.Repository
import com.gdalamin.bcs_pro.repository.SubjectNameRepository
import com.gdalamin.bcs_pro.util.Constants
import com.gdalamin.bcs_pro.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val repository: Repository,
    private val subjectRepository: SubjectNameRepository,
    @ApplicationContext private val context: Context

) : ViewModel() {
    val pageNumber = 1
    private val _subjects: MutableLiveData<Resource<MutableList<SubjectName>>> = MutableLiveData()
    val subjectName: LiveData<Resource<MutableList<SubjectName>>> = _subjects

//    private val networkUtil = NetworkUtils(context)


    fun getSubjectNameN(apiNumber: Int) {
        viewModelScope.launch {
            if (subjectRepository.isDatabaseEmpty()) {
                _subjects.postValue(Resource.Loading())
                try {
                    val response = repository.getSubjects(
                        apiNumber, pageNumber,
                        Constants.PAGE_SIZE
                    )
                    val result = handleResponseApi(response)
                    _subjects.postValue(handleResponseApi(response))

                    if (result is Resource.Success) {
                        saveExamsToDatabase(result.data)
                    }

                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _subjects.postValue(Resource.Error("Network Failure"))
                        else -> _subjects.postValue(Resource.Error("Conversion Error"))
                    }
                }
            } else {
                _subjects.postValue(
                    Resource.Success(
                        subjectRepository.getAllExamsNonLive().toMutableList()
                    )
                )
            }
        }
    }

    private fun handleResponseApi(response: Response<MutableList<SubjectName>>): Resource<MutableList<SubjectName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun saveExamsToDatabase(exams: MutableList<SubjectName>?) {
        exams?.let {
            subjectRepository.insertAll(it)
        }
    }


    fun getSubjectNameDatabase(): LiveData<List<SubjectName>> {
        return subjectRepository.getAllExams()
    }

//    private fun hasInternetConnection(): Boolean {
//        return true
//    }


    ///////////////////////////////////////////////////////////////////////
    suspend fun getSubjectName(apiNumber: Int) {
        _subjects.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val questionResponse = repository.getSubjects(
                    apiNumber, pageNumber,
                    Constants.PAGE_SIZE
                )
                _subjects.postValue(handleSubjectResponse(questionResponse))
            } else {
                _subjects.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _subjects.postValue(Resource.Error("Network Failure"))
                else -> _subjects.postValue(Resource.Error("Conversion Error "))
            }
        }
    }

    private fun handleSubjectResponse(
        response: Response<MutableList<SubjectName>>
    ): Resource<MutableList<SubjectName>> {
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