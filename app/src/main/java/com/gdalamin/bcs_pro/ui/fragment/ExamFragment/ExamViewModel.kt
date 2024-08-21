package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.remote.paging.ExamQuestionPagingSource
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.ui.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _questions: MutableLiveData<Resource<MutableList<Question>>> = MutableLiveData()
    val questions: LiveData<Resource<MutableList<Question>>> = _questions
    private var isDataLoaded = false


    private var _questionsPaging: Flow<PagingData<Question>> = flowOf()
    val questionsPaging: Flow<PagingData<Question>> get() = _questionsPaging
    private var _isDataLoaded2 = MutableStateFlow(false)


    // Map each exam type to its corresponding question amounts
    private val questionAmountMap = mapOf(
        50 to listOf(5, 7, 9, 3, 3, 4, 8, 4, 3, 4),
//        50 to listOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
        100 to listOf(10, 15, 18, 5, 5, 7, 17, 8, 7, 8),
        200 to listOf(20, 30, 35, 10, 10, 15, 35, 15, 15, 15)
    )


    fun getExamQuestions(examType: Int) {
        if (!_isDataLoaded2.value) {
            // Get the corresponding question amounts for the selected exam type
            val questionAmounts = questionAmountMap[examType] ?: emptyList()

            _questionsPaging =
                Pager(
                    PagingConfig(
                        pageSize = questionAmounts.size, // Page size is the total number of pages
                        enablePlaceholders = false,
                        prefetchDistance = 1 // Fetch next page in advance
                    )
                ) {
                    ExamQuestionPagingSource(examRepository, questionAmounts)
                }.flow.cachedIn(viewModelScope)
            _isDataLoaded2.value = true
        }
    }

    suspend fun getSubjectExamQuestions(subjectName: String, totalQuestion: Int) {
        if (!isDataLoaded) {
            _questions.postValue(Resource.Loading())
            try {
                val questionResponse =
                    examRepository.getSubjectBasedExamQuestion(subjectName, totalQuestion)
                _questions.postValue(handleQuestionResponse(questionResponse))
                isDataLoaded = true
            } catch (t: Throwable) {
                handleThrowable(t)
            }
        }
    }

    private fun handleQuestionResponse(response: Response<MutableList<Question>>): Resource<MutableList<Question>> {
        return if (response.isSuccessful) {

            response.body()?.let {

                Resource.Success(it)

            } ?: Resource.Error("No data")
        } else {
            Resource.Error(response.message())
        }
    }


    private fun handleThrowable(t: Throwable) {
        _questions.postValue(
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        )
    }


}