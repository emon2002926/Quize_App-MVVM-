package com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.local.repositories.LocalSubjectRepository
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.data.remote.repositories.SubjectRepository
import com.gdalamin.bcs_pro.ui.utilities.Constants
import com.gdalamin.bcs_pro.ui.utilities.Resource
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
    private val _subjects: MutableLiveData<Resource<MutableList<SubjectName>>> = MutableLiveData()
    val subjectName: LiveData<Resource<MutableList<SubjectName>>> = _subjects

    fun getSubjectsName(apiNumber: Int) {
        viewModelScope.launch {
            if (localSubjectRepository.isDatabaseEmpty()) {
                _subjects.postValue(Resource.Loading())
                try {
                    val response = subjectRepository.getSubjects(
                        apiNumber, pageNumber,
                        Constants.PAGE_SIZE
                    )
                    val result = handleResponseApi(response)
                    _subjects.postValue(handleResponseApi(response))

                    if (result is Resource.Success) {
                        saveSubjectNameToDatabase(result.data)
                    }

                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _subjects.postValue(Resource.Error("Network Failure"))
                        else -> _subjects.postValue(Resource.Error("Conversion Error"))
                    }
                }
            } else {
                _subjects.postValue(
                    Resource.Success(
                        localSubjectRepository.getAllExamsNonLive().toMutableList()
                    )
                )
            }
        }
    }

    private fun handleResponseApi(response: Response<MutableList<SubjectName>>): Resource<MutableList<SubjectName>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun saveSubjectNameToDatabase(exams: MutableList<SubjectName>?) {
        exams?.let {
            localSubjectRepository.insertAll(it)
        }
    }


    fun getSubjectNameDatabase(): LiveData<List<SubjectName>> {
        return localSubjectRepository.getAllExams()
    }


}