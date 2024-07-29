package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import QuestionPagingSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gdalamin.bcs_pro.model.Question
import com.gdalamin.bcs_pro.repository.Repository
import com.gdalamin.bcs_pro.util.Constants.Companion.QUESTION_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class QuestionViewModelTest @Inject constructor(private val repository: Repository) : ViewModel() {


//    val questions: Flow<PagingData<Question>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
//        QuestionPagingSource(repository)
//    }.flow.cachedIn(viewModelScope)


    private var _questions: Flow<PagingData<Question>> = flowOf()
    val questions: Flow<PagingData<Question>> get() = _questions

    private var isDataLoaded = false
    fun getQuestions(apiNumber: Int, batch: String? = null) {

        if (!isDataLoaded) {
            when (apiNumber) {
                1 -> {
                    _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
                        QuestionPagingSource(repository, apiNumber)
                    }.flow.cachedIn(viewModelScope)
                    isDataLoaded = true
                }

                9 -> {
                    _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
                        QuestionPagingSource(repository, apiNumber, batch)
                    }.flow.cachedIn(viewModelScope)
                    isDataLoaded = true
                }

                else -> {}
            }


        }
    }


}