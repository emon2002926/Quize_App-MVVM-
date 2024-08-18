package com.gdalamin.bcs_pro.data.remote.api

import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val questionApi: QuestionApi by lazy {
        retrofit.create(QuestionApi::class.java)
    }

    val examApi: ExamApi by lazy {
        retrofit.create(ExamApi::class.java)
    }
    val questionBankApi: QuestionBankApi by lazy {
        retrofit.create(QuestionBankApi::class.java)
    }
    val subjectsApi: SubjectsApi by lazy {
        retrofit.create(SubjectsApi::class.java)
    }


}