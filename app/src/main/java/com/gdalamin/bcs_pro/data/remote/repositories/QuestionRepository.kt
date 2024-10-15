package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.local.dao.QuestionDao
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.api.QuestionApi
import com.gdalamin.bcs_pro.utilities.Constants.Companion.API_KEY
import com.gdalamin.bcs_pro.utilities.Constants.Companion.DEFAULT_PAGE_NUMBER
import com.gdalamin.bcs_pro.utilities.Constants.Companion.YEAR_QUESTION_DOWNLOAD_API
import retrofit2.Response
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionApi: QuestionApi,
    private val questionDao: QuestionDao
) {
    
    suspend fun getQuestion(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<Question>> {
        return questionApi.getQuestions(API_KEY, apiNumber, pageNumber, limit)
    }
    
    suspend fun forYearQuestionDownload(
        limit: Int,
        batch: String
    ): Response<MutableList<Question>> {
        return questionApi.forYearQuestionDownload(
            API_KEY,
            YEAR_QUESTION_DOWNLOAD_API,
            DEFAULT_PAGE_NUMBER,
            limit,
            batch
        )
    }
    
    suspend fun getPreviousYearQuestion(
        batch: String,
        page: Int
    ): Response<MutableList<Question>> {
        return questionApi.getPreviousYearQuestions(API_KEY, batch, page)
    }
    
    suspend fun getSubjectBasedQuestions(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int,
        subjectName: String
    ): Response<MutableList<Question>> {
        return questionApi.getSubjectBasedQuestions(
            API_KEY,
            apiNumber,
            pageNumber,
            limit,
            subjectName
        )
    }
    
    fun addQuestions(question: List<Question>) {
        questionDao.addQuestion(question)
        
    }
    
    fun getQuestionsByBatch(batchName: String): List<Question> {
        return questionDao.getQuestionsByBatch(batchName)
    }

//    val subjects = listOf("IA", "BA", "BLL", "MVG", "GEDM", "ML", "ELL", "MA", "GS", "ICT")
//
//    fun getQuestionsByBatchAndSubjects(batch: String): List<Question> {
//        // Fetch all questions by batch and subjects
//        val questions = questionDao.getQuestionsByBatchAndSubjects(batch, subjects)
//
//        // Sort questions by subject order
//        val sortedQuestions = questions.sortedBy { question ->
//            // Find the index of the question's subject in the subject list
//            subjects.indexOf(subjects)
//        }
//
//        return sortedQuestions
//    }
    
    val subjects = listOf("IA", "BA", "BLL", "MVG", "GEDM", "ML", "ELL", "MA", "GS", "ICT")
    
    fun getQuestionsByBatchAndSubjects(batch: String): List<Question> {
        // Fetch all questions by batch and subjects
        val questions = questionDao.getQuestionsByBatchAndSubjects(batch, subjects)
        
        // Sort questions by subject order
        val sortedQuestions = questions.sortedBy { question ->
            // Find the index of the question's subject in the subject list
            subjects.indexOf(question.subjects)
        }
        
        return sortedQuestions
    }
    
}