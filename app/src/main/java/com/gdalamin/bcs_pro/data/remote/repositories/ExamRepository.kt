package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.local.dao.QuestionDao
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.api.ExamApi
import com.gdalamin.bcs_pro.utilities.Constants.Companion.API_KEY
import com.gdalamin.bcs_pro.utilities.Constants.Companion.LIVE_EXAM_API
import com.gdalamin.bcs_pro.utilities.Constants.Companion.SUBJECT_BASED_EXAM
import retrofit2.Response
import javax.inject.Inject


class ExamRepository @Inject constructor(
    private val examApi: ExamApi, private val questionDao: QuestionDao
) {

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
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<LiveExam>> {
        return examApi.getExamInfo(API_KEY, LIVE_EXAM_API, pageNumber, limit)
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
    
    suspend fun getLiveExamQuestions(
        questionSet: String,
        page: Int
    ): Response<MutableList<Question>> {
        return examApi.getLiveExamQuestions(
            "abc123",
            questionSet,
            page
        )
    }


//    suspend fun addQuestions(question: List<Question>) {
//        questionDao.addQuestion(question)
//
//    }
//
//    suspend fun getQuestion(): List<Question> {
//        return questionDao.getQuestion()
//    }
    
}