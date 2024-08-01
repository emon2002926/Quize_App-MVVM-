package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.api.QuestionApi
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionApi: QuestionApi) {

    suspend fun getQuestion(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<Question>> {
        return questionApi.getQuestions(API_KEY, apiNumber, pageNumber, limit)
    }

    suspend fun getPreviousYearQuestion(
        apiNumber: Int,
        pageNumber: Int,
        limit: Int,
        batch: String
    ): Response<MutableList<Question>> {
        return questionApi.getPreviousYearQuestions(API_KEY, apiNumber, pageNumber, limit, batch)
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

}