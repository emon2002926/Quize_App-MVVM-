package com.example.bcsprokotlin.repository

import com.example.bcsprokotlin.api.ApiService
import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.model.LiveExam
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.model.SubjectName
import com.example.bcsprokotlin.util.Constants.Companion.API_KEY
import com.example.bcsprokotlin.util.Constants.Companion.NORMAL_EXAM
import com.example.bcsprokotlin.util.Constants.Companion.SUBJECT_BASED_EXAM
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun getBcsYearName(
        apiNumber: Int, pageNumber: Int, limit: Int
    ): Response<MutableList<BcsYearName>> {
        return apiService.getBcsYearName(API_KEY, apiNumber, pageNumber, limit)
    }

    suspend fun getSubjects(
        apiNumber: Int, pageNumber: Int, limit: Int
    ): Response<MutableList<SubjectName>> {
        return apiService.getSubjects(API_KEY, apiNumber, pageNumber, limit)
    }

    suspend fun getQuestion(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<Question>> {
        return apiService.getQuestions(API_KEY, apiNumber, pageNumber, limit)
    }


    suspend fun getPreviousYearQuestion(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int,
        batch: String
    ): Response<MutableList<Question>> {
        return apiService.getPreviousYearQuestions(API_KEY, apiNumber, pageNumber, limit, batch)
    }

    suspend fun getExamInfo(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<LiveExam>> {
        return apiService.getExamInfo(API_KEY, apiNumber, pageNumber, limit)
    }

    suspend fun getExamQuestion(
        totalQuestions: Int

    ): Response<MutableList<Question>> {
        return apiService.getExamQuestions(
            API_KEY,
            NORMAL_EXAM,
            totalQuestions
        )
    }

    suspend fun getSubjectBasedExamQuestion(
        subjectName: String,
        totalQuestion: Int
    ): Response<MutableList<Question>> {
        return apiService.getSubjectBasedExamQuestions(
            API_KEY,
            SUBJECT_BASED_EXAM,
            subjectName,
            totalQuestion
        )
    }


}