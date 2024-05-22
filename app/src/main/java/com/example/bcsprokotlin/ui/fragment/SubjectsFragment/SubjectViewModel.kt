package com.example.bcsprokotlin.ui.fragment.SubjectsFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.util.Constants
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val pageNumber = 1


    val subjects: MutableLiveData<Resource<MutableList<SubjectName>>> = MutableLiveData()

    suspend fun getSubjectName(apiNumber: Int) {
        subjects.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val questionResponse = repository.getSubjects(
                    apiNumber, pageNumber,
                    Constants.PAGE_SIZE
                )
                subjects.postValue(handleSubjectResponse(questionResponse))
            } else {
                subjects.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> subjects.postValue(Resource.Error("Network Failure"))
                else -> subjects.postValue(Resource.Error("Conversion Error "))
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