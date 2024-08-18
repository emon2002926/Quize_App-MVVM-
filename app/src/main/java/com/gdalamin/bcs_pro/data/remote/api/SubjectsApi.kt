package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SubjectsApi {

    @GET("/api/appUtil.php")
    suspend fun getSubjects(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<SubjectName>>

}