package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.data.remote.api.SubjectsApi
import com.gdalamin.bcs_pro.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject

class SubjectRepository @Inject constructor(private val subjectsApi: SubjectsApi) {
    
    suspend fun getSubjects(
        apiNumber: Int, pageNumber: Int, limit: Int
    ): Response<MutableList<SubjectName>> {
        return subjectsApi.getSubjects(API_KEY, apiNumber, pageNumber, limit)
    }
}