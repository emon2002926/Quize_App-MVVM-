package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.paging.QuestionPagingSource
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.QUESTION_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class QuestionViewMode @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {
    private var _questions: Flow<PagingData<Question>> = flowOf()
    val questions: Flow<PagingData<Question>> get() = _questions

    private var isDataLoaded = false

    fun getQuestions(apiNumber: Int, batchOrSubjectName: String? = null) {
        if (!isDataLoaded) {
            _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
                QuestionPagingSource(
                    questionRepository,
                    apiNumber,
                    batchOrSubjectName
                )
            }.flow.cachedIn(viewModelScope)
            isDataLoaded = true
        }
    }

}