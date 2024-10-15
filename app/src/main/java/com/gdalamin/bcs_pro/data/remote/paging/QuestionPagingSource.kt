package com.gdalamin.bcs_pro.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository

class QuestionPagingSource(
    private val questionRepository: QuestionRepository,
    private val apiNumber: Int,
    private val batchOrSubjectName: String? = null // Optional parameter for batch if needed
) : PagingSource<Int, Question>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {
        val pageNumber = params.key ?: 1
        val limit = params.loadSize
        
        return try {
            val response = when (apiNumber) {
                1 -> questionRepository.getQuestion(apiNumber, pageNumber, limit)
                
                else -> questionRepository.getSubjectBasedQuestions(
                    apiNumber,
                    pageNumber,
                    limit,
                    batchOrSubjectName ?: ""
                )

//                else -> questionRepository.getPreviousYearQuestion(
//                    apiNumber,
//                    pageNumber,
//                    limit,
//                    batchOrSubjectName ?: ""
//                )
            }
            
            val questions = response.body() ?: mutableListOf()
            LoadResult.Page(
                data = questions,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (questions.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Question>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}