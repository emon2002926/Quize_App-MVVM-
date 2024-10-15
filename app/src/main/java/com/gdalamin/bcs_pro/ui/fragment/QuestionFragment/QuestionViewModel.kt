package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.paging.PreviousQuestionPagingSource
import com.gdalamin.bcs_pro.data.remote.paging.QuestionPagingSource
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.Constants.Companion.QUESTION_PAGE_SIZE
import com.gdalamin.bcs_pro.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private var _questions: Flow<PagingData<Question>> = flowOf()
    val questions: Flow<PagingData<Question>> get() = _questions
    
    private val _yearQuestion: MutableLiveData<DataState<MutableList<Question>>> =
        MutableLiveData()
    val yearQuestion: LiveData<DataState<MutableList<Question>>> = _yearQuestion
    
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
    
    fun getPreviousQuestions(batchName: String) {
        
        if (!isDataLoaded) {
            _questions = Pager(PagingConfig(pageSize = QUESTION_PAGE_SIZE)) {
                PreviousQuestionPagingSource(
                    questionRepository,
                    batchName
                )
            }.flow.cachedIn(viewModelScope)
            isDataLoaded = true
        }
    }
    
    fun getQuestionsByBatch(batchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (questionRepository.getQuestionsByBatchAndSubjects(batchName)
                    .isNotEmpty()
            ) {
                _yearQuestion.postValue(
                    DataState.Success(
                        questionRepository.getQuestionsByBatchAndSubjects(
                            batchName
                        ).toMutableList()
                    )
                )
            } else {
            
            }
            
        }
        
    }
    
    
    fun getYearQuestion(batchName: String, totalQuestion: Int) {
        viewModelScope.launch {
            _yearQuestion.postValue(DataState.Loading())
            try {
                
                val response = questionRepository.forYearQuestionDownload(totalQuestion, batchName)
                _yearQuestion.postValue(handleQuestionResponse(response))
                response.apply {
                    if (isSuccessful) {
                        body()?.let {
                            saveQuestions(it)
                        }
                    }
                }
                
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _yearQuestion.postValue(
                        DataState.Error(
                            CHECK_INTERNET_CONNECTION_MESSAGE
                        )
                    )
                }
            }
        }
    }
    
    private fun handleQuestionResponse(response: Response<MutableList<Question>>): DataState<MutableList<Question>> {
        return if (response.isSuccessful) {
            
            response.body()?.let {
                
                DataState.Success(it)
                
            } ?: DataState.Error("No data")
        } else {
            DataState.Error(response.message())
        }
    }
    
    fun saveQuestions(question: MutableList<Question>) {
        viewModelScope.launch(Dispatchers.IO) {
            question.let {
                questionRepository.addQuestions(question)
            }
        }
    }
    
    
}