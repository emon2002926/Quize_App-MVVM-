package com.gdalamin.bcs_pro.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.repository.Repository

class QuestionPagingSource(
    private val repository: Repository,
    private val apiNumber: Int,
    private val batch: String? = null // Optional parameter for batch if needed
) : PagingSource<Int, Question>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {
        val pageNumber = params.key ?: 1
        val limit = params.loadSize

        return try {
            val response = if (apiNumber == 1) {
                repository.getQuestion(apiNumber, pageNumber, limit)
            } else {
                repository.getPreviousYearQuestion(apiNumber, pageNumber, limit, batch ?: "")
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