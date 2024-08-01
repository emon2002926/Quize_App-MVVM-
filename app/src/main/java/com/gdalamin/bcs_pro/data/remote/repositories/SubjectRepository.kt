package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.data.remote.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class SubjectRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSubjects(
        apiNum: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<SubjectName>> {
        return apiService.getSubjects(apiNum = apiNum, pageNumber = pageNumber, limit = limit)
    }
}