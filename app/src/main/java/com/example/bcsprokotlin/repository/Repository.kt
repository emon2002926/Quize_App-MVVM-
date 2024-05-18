package com.example.bcsprokotlin.repository

import com.example.bcsprokotlin.api.QuestionApi
import com.example.bcsprokotlin.api.RetrofitInstance
import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectName
import com.example.bcsprokotlin.util.Constants
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(    private val questionApi: QuestionApi) {

    suspend fun getBcsYearName(
        apiNumber: Int
        ,pageNumber: Int
        ,limit: Int): Response<MutableList<BcsYearName>> {
        return questionApi.getBcsYearName(Constants.API_KEY,apiNumber,pageNumber, limit)
    }

    suspend fun getSubjects(
        apiNumber: Int
        ,pageNumber: Int
        ,limit: Int): Response<MutableList<SubjectName>> {
        return questionApi.getSubjects(Constants.API_KEY,apiNumber,pageNumber, limit)
    }

    suspend fun getQuestion( apiNumber: Int,pageNumber:Int,limit:Int): Response<MutableList<Question>> {
        return RetrofitInstance.api.getQuestions(Constants.API_KEY, apiNumber,pageNumber,limit)
    }

}