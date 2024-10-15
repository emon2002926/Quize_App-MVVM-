package com.gdalamin.bcs_pro.data.model

import androidx.annotation.Keep

@Keep
data class SharedData(
    val title: String = "",
    val action: String = "",
    val totalQuestion: Int = 0,
    val questionType: String = "",
    val batchOrSubjectName: String = "",
    val time: Int = 0,
    val isSavedOnDatabase: Boolean = false
)
