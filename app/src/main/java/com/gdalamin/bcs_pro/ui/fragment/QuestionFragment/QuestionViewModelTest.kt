package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.paging.QuestionPagingSource
import com.gdalamin.bcs_pro.data.repository.Repository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.QUESTION_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class QuestionViewModelTest @Inject constructor(private val repository: Repository) : ViewModel() {
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


//    private var _questions: Flow<PagingData<Question>> = flowOf()
//    val questions: Flow<PagingData<Question>> get() = _questions
//
//    private var isDataLoaded = false
//    private var apiNumber = 0
//    private var batch: String? = null
//
//    fun getQuestions(apiNumber: Int, batch: String? = null) {
//        this.apiNumber = apiNumber
//        this.batch = batch
//
//        if (!isDataLoaded) {
//            loadQuestions()
//            isDataLoaded = true
//        }
//    }
//
//    fun refreshQuestions() {
//        loadQuestions()
//    }
//
//    private fun loadQuestions() {
//        _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
//            com.gdalamin.bcs_pro.data.paging.QuestionPagingSource(repository, apiNumber, batch)
//        }.flow.cachedIn(viewModelScope)
//    }


    ///Old one
//    val questions: Flow<PagingData<Question>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
//        com.gdalamin.bcs_pro.data.paging.QuestionPagingSource(repository)
//    }.flow.cachedIn(viewModelScope)

//    private var _questions: Flow<PagingData<Question>> = flowOf()
//    val questions: Flow<PagingData<Question>> get() = _questions
//
//    private var isDataLoaded = false
//    fun getQuestions(apiNumber: Int, batch: String? = null) {
//
//        if (!isDataLoaded) {
//            when (apiNumber) {
//                1 -> {
//                    _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
//                        com.gdalamin.bcs_pro.data.paging.QuestionPagingSource(repository, apiNumber)
//                    }.flow.cachedIn(viewModelScope)
//                    isDataLoaded = true
//                }
//
//                9 -> {
//                    _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
//                        com.gdalamin.bcs_pro.data.paging.QuestionPagingSource(repository, apiNumber, batch)
//                    }.flow.cachedIn(viewModelScope)
//                    isDataLoaded = true
//                }
//
//                else -> {}
//            }
//
//
//        }
//    }


}