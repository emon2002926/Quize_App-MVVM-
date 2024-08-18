package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApi {

    @GET("/api/appUtil.php")
    suspend fun getQuestions(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<Question>>

    @GET("/api/appUtil.php")
    suspend fun getPreviousYearQuestions(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
        @Query("batch") batch: String
    ): Response<MutableList<Question>>

    @GET("/api/appUtil.php")
    suspend fun getSubjectBasedQuestions(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
        @Query("subjectName") subjectName: String
    ): Response<MutableList<Question>>

}