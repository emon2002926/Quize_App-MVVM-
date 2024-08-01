package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.remote.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class BcsYearRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getBcsYearName(
        apiNum: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<BcsYearName>> {
        return apiService.getBcsYearName(apiNum = apiNum, pageNumber = pageNumber, limit = limit)
    }
}