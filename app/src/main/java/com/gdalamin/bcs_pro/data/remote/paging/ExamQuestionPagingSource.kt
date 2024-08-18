package com.gdalamin.bcs_pro.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository

class ExamQuestionPagingSource(
    private val examRepository: ExamRepository,
    private val questionAmounts: List<Int> // List of question amounts for each page
) : PagingSource<Int, Question>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {
        val pageNumber = params.key ?: 1 // Current subject page

        // Ensure the page number does not exceed the size of the questionAmounts list
        if (pageNumber > questionAmounts.size) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        val amount = questionAmounts[pageNumber - 1] // Get the amount for this page

        return try {
            val response = examRepository.getExamQuestions(amount, pageNumber)
            val questions = response.body() ?: emptyList()

            LoadResult.Page(
                data = questions,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (questions.isEmpty() || pageNumber >= questionAmounts.size) null else pageNumber + 1
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
