package com.example.bcsprokotlin.api

import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApi {

    @GET("/api/getData2.php")
    suspend fun getQuestions(
        @Query("apiKey")
        apiKey: String="abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,

    ): Response<MutableList<Question>>

    @GET("/api/getData2.php")
    suspend fun getSubjects(
        @Query("apiKey")
        apiKey: String="abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,

        ): Response<MutableList<SubjectName>>

    @GET("/api/getData2.php")
    suspend fun getBcsYearName(
        @Query("apiKey")
        apiKey: String="abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,

        ): Response<MutableList<BcsYearName>>


    @GET("/api/getData2.php")
    suspend fun getExam(
        @Query("apiKey")
        apiKey: String="abc123",
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
        ): Response<MutableList<SubjectName>>

}