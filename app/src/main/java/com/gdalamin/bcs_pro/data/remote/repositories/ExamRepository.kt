package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.api.ExamApi
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.SUBJECT_BASED_EXAM
import retrofit2.Response
import javax.inject.Inject


class ExamRepository @Inject constructor(private val examApi: ExamApi) {

//    suspend fun getExamQuestion(
//        totalQuestions: Int
//    ): Response<MutableList<Question>> {
//        return examApi.getExamQuestions(
//            API_KEY,
//            NORMAL_EXAM,
//            totalQuestions
//        )
//    }

    suspend fun getSubjectBasedExamQuestion(
        subjectName: String? = null,
        totalQuestion: Int
    ): Response<MutableList<Question>> {
        return examApi.getSubjectBasedExamQuestions(
            API_KEY,
            SUBJECT_BASED_EXAM,
            subjectName,
            totalQuestion
        )
    }

    suspend fun getExamInfo(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<LiveExam>> {
        return examApi.getExamInfo(API_KEY, apiNumber, pageNumber, limit)
    }


    suspend fun getExamQuestions(
        amount: Int,
        page: Int
    ): Response<MutableList<Question>> {
        return examApi.getExamQuestions(
            API_KEY,
            amount,
            page
        )
    }


}