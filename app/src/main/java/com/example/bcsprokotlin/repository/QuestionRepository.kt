package com.example.bcsprokotlin.repository

import com.example.bcsprokotlin.api.RetrofitInstance
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SubjectName
import com.example.bcsprokotlin.util.Constants.Companion.API_KEY
import retrofit2.Response

class QuestionRepository {
    suspend fun getQuestion( apiNumber: Int,pageNumber:Int,limit:Int): Response<MutableList<Question>> {
        return RetrofitInstance.api.getQuestions(API_KEY, apiNumber,pageNumber,limit)
    }

    suspend fun getSubjects(
        apiNumber: Int
        ,pageNumber: Int
        ,limit: Int):Response<MutableList<SubjectName>>{
        return RetrofitInstance.api.getSubjects(API_KEY,apiNumber,pageNumber, limit)
    }

}