package com.gdalamin.bcs_pro

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

//        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            OneSignal.Notifications.requestPermission(false)
//        }
    }
}
