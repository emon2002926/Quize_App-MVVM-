package com.gdalamin.bcs_pro.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository

class PreviousQuestionPagingSource(
    private val questionRepository: QuestionRepository,
    private val batchName: String
) : PagingSource<Int, Question>() {
    
    companion object {
        const val MAX_PAGES = 10 // Maximum page number allowed
    }
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {
        val pageNumber = params.key ?: 1 // Default to the first page if no key is provided
        
        // Ensure that the page number is within the valid range
        if (pageNumber > MAX_PAGES) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = null
            )
        }
        
        return try {
            // Call the repository with the batch name and page number
            val response = questionRepository.getPreviousYearQuestion(batchName, pageNumber)
            val questions = response.body() ?: mutableListOf()
            
            LoadResult.Page(
                data = questions,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (questions.isEmpty() || pageNumber == MAX_PAGES) null else pageNumber + 1
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