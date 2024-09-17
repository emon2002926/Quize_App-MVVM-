plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
//    id ("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    alias(libs.plugins.googleGmsGoogleServices)
    
    
}

//com.gdalamin.bcs_pro
android {
    namespace = "com.gdalamin.bcs_pro"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.gdalamin.bcs_pro"
        minSdk = 24
        targetSdk = 34
        versionCode = 70
        versionName = "2.4.00"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")
    
    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    
    
    //paging library
    implementation("androidx.paging:paging-runtime:3.3.2") // Replace with the latest version
    
    //OneSignal
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")
    
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    
    
}