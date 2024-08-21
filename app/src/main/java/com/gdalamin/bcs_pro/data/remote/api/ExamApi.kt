package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExamApi {

    @GET("/api/pagingExam.php")
    suspend fun getExamQuestions(
        @Query("apiKey") apiKey: String,
        @Query("amount") amount: Int,
        @Query("page") page: Int
    ): Response<MutableList<Question>>


    @GET("/api/exam.php")
    suspend fun getSubjectBasedExamQuestions(
        @Query("apiKey") apiKey: String,
        @Query("exam_type") examType: String,
        @Query("subject_name") subjectName: String? = null,
        @Query("totalQuestions") questionAmount: Int,
    ): Response<MutableList<Question>>


    @GET("/api/appUtil.php")
    suspend fun getExamInfo(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
    ): Response<MutableList<LiveExam>>
}