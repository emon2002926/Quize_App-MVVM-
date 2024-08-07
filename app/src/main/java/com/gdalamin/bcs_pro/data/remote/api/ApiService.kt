package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.data.model.SubjectName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("/api/appUtil.php")
    suspend fun getSubjects(
        @Query("apiKey")
        apiKey: String = "abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,

        ): Response<MutableList<SubjectName>>

//    @GET("/api/appUtil.php")
//    suspend fun getBcsYearName(
//        @Query("apiKey")
//        apiKey: String = "abc123",
//        @Query("apiNum") apiNum: Int,
//        @Query("page") pageNumber: Int,
//        @Query("limit") limit: Int,
//    ): Response<MutableList<BcsYearName>>
//
//
//    @GET("/api/appUtil.php")
//    suspend fun getExamInfo(
//        @Query("apiKey")
//        apiKey: String = "abc123",
//        @Query("apiNum") apiNum: Int,
//        @Query("page") pageNumber: Int,
//        @Query("limit") limit: Int,
//    ): Response<MutableList<LiveExam>>


    /*

    @GET("/api/appUtil.php")
    suspend fun getQuestions(
        @Query("apiKey")
        apiKey: String = "abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,

        ): Response<MutableList<Question>>


    @GET("/api/appUtil.php")
    suspend fun getPreviousYearQuestions(
        @Query("apiKey")
        apiKey: String = "abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
        @Query("batch") batch: String,

        ): Response<MutableList<Question>>

    @GET("/api/exam.php")
    suspend fun getExamQuestions(
        @Query("apiKey") apiKey: String,
        @Query("exam_type") questionType: String,
        @Query("totalQuestions") totalQuestions: Int,
    ): Response<MutableList<Question>>


    @GET("/api/exam.php")
    suspend fun getSubjectBasedExamQuestions(
        @Query("apiKey") apiKey: String,
        @Query("exam_type") examType: String,
        @Query("subject_name") subjectName: String,
        @Query("totalQuestions") questionAmount: Int,
    ): Response<MutableList<Question>>

     */
}
