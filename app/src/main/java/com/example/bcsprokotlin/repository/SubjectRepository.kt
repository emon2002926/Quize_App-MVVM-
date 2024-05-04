package com.example.bcsprokotlin.repository

import com.example.bcsprokotlin.api.RetrofitInstance
import com.example.bcsprokotlin.model.SubjectName
import com.example.bcsprokotlin.util.Constants
import retrofit2.Response

class SubjectRepository {

    suspend fun getSubjects(
        apiNumber: Int
        ,pageNumber: Int
        ,limit: Int): Response<MutableList<SubjectName>> {
        return RetrofitInstance.api.getSubjects(Constants.API_KEY,apiNumber,pageNumber, limit)
    }

}