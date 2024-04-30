package com.example.bcsprokotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SubjectName
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.util.Constants.Companion.PAGE_SIZE
import com.example.bcsprokotlin.util.Resource
import okio.IOException
import retrofit2.Response

class QuestionViewModel:ViewModel() {
    private val questionRepository = Repository()

    private val apiNumber=1
    val pageNumber= 1

    val questions: MutableLiveData<Resource<MutableList<Question>>> = MutableLiveData()
    val subjects: MutableLiveData<Resource<MutableList<SubjectName>>> = MutableLiveData()

     suspend fun getQuestion(){
        questions.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val questionResponse =  questionRepository.getQuestion(apiNumber ,pageNumber,PAGE_SIZE)
                questions.postValue(handleQuestionResponse(questionResponse))
            }else{
                questions.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->questions.postValue(Resource.Error("Network Failure"))
                else->questions.postValue(Resource.Error("Conversion Error "))
            }
        }
    }

    private fun handleQuestionResponse(response :Response<MutableList<Question>>):Resource<MutableList<Question>>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun getSubjects(apiNumber:Int){
        questions.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val questionResponse =  questionRepository.getSubjects(apiNumber ,pageNumber,PAGE_SIZE)
                subjects.postValue(handleSubjectResponse(questionResponse))
            }else{
                subjects.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->subjects.postValue(Resource.Error("Network Failure"))
                else->subjects.postValue(Resource.Error("Conversion Error "))
            }
        }
    }

    private fun handleSubjectResponse(response :Response<MutableList<SubjectName>>
    ):Resource<MutableList<SubjectName>>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }





    private fun hasInternetConnection():Boolean{
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