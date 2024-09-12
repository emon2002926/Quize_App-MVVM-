package com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalSubjectRepository
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.data.remote.repositories.SubjectRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.DEFAULT_PAGE_NUMBER
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.PAGE_SIZE
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.SUBJECT_API
import com.gdalamin.bcs_pro.ui.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val localSubjectRepository: LocalSubjectRepository
) : ViewModel() {
    val pageNumber = 1
    private val _subjects: MutableLiveData<DataState<MutableList<SubjectName>>> = MutableLiveData()
    val subjectName: LiveData<DataState<MutableList<SubjectName>>> = _subjects
    
    fun getSubjectsName() {
        viewModelScope.launch {
            if (localSubjectRepository.isDatabaseEmpty()) {
                _subjects.postValue(DataState.Loading())
                try {
                    val response =
                        subjectRepository.getSubjects(SUBJECT_API, DEFAULT_PAGE_NUMBER, PAGE_SIZE)
                    val result = handleResponseApi(response)
                    _subjects.postValue(handleResponseApi(response))
                    
                    if (result is DataState.Success) {
                        saveSubjectNameToDatabase(result.data)
                    }
                    
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _subjects.postValue(
                            DataState.Error(
                                CHECK_INTERNET_CONNECTION_MESSAGE
                            )
                        )
                        
                    }
                }
            } else {
                _subjects.postValue(
                    DataState.Success(
                        localSubjectRepository.getAllSubjectFromDB().toMutableList()
                    )
                )
            }
        }
    }
    
    
    private fun handleResponseApi(response: Response<MutableList<SubjectName>>): DataState<MutableList<SubjectName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return DataState.Success(it)
            }
        }
        return DataState.Error(response.message())
    }
    
    
    private suspend fun saveSubjectNameToDatabase(exams: MutableList<SubjectName>?) {
        exams?.let {
            localSubjectRepository.insertAll(it)
        }
    }
    
    
}