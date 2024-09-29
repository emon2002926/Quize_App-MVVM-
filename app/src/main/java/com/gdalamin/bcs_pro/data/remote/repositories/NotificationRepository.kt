package com.gdalamin.bcs_pro.data.remote.repositories

import com.gdalamin.bcs_pro.data.model.UserNotification
import com.gdalamin.bcs_pro.data.remote.api.UserNotificationApi
import com.gdalamin.bcs_pro.utilities.Constants.Companion.API_KEY
import com.gdalamin.bcs_pro.utilities.Constants.Companion.NOTIFICATION_API
import com.gdalamin.bcs_pro.utilities.Constants.Companion.PAGE_SIZE
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationApi: UserNotificationApi) {
    
    suspend fun getNotification(pageNumber: Int): Response<MutableList<UserNotification>> {
        return notificationApi.getNotification(API_KEY, NOTIFICATION_API, pageNumber, PAGE_SIZE)
    }
}