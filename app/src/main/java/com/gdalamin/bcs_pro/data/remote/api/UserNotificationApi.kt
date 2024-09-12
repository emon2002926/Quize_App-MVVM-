package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.data.model.UserNotification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserNotificationApi {
    @GET("/api/appUtil.php")
    suspend fun getNotification(
        @Query("apiKey") apiKey: String,
        @Query("apiNum") apiNum: Int,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<UserNotification>>
}