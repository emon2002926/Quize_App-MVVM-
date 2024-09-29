package com.gdalamin.bcs_pro

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        //todo have to remove this comment for ad
        MobileAds.initialize(this) {}
    }
}
