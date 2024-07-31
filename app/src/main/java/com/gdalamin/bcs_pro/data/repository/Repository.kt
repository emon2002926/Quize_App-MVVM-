package com.gdalamin.bcs_pro.data.repository

import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.data.remote.api.ApiService
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.NORMAL_EXAM
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.SUBJECT_BASED_EXAM
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

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


}