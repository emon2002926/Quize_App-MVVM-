package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.data.remote.api.QuestionBankApi
import retrofit2.Response
import javax.inject.Inject

class QuestionBankRepository @Inject constructor(private val questionBankApi: QuestionBankApi) {

    suspend fun getBcsYearName(
        apiNum: Int,
        pageNumber: Int,
        limit: Int
    ): Response<MutableList<BcsYearName>> {
        return questionBankApi.getBcsYearName(
            apiNum = apiNum,
            pageNumber = pageNumber,
            limit = limit
        )
    }
}