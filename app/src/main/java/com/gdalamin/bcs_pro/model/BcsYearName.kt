package com.gdalamin.bcs_pro.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class BcsYearName(
    val bcsYearName: String,
    val dailyExam: String,
    val id: Int,
    val subjectCode: String,
    val subjects: String,
    val totalQuestion: String
) : Serializable