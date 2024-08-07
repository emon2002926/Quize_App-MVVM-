package com.gdalamin.bcs_pro.data.model

import androidx.annotation.Keep

@Keep
data class SharedData(
    val title: String,
    val action: String,
    val totalQuestion: Int,
    val questionType: String,
    val batchOrSubjectName: String,
    val time: Int
)
