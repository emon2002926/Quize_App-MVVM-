package com.gdalamin.bcs_pro.data.model

import androidx.annotation.Keep

@Keep
data class ExamResult(
    val subjectName: String,
    val mark: Double,
    val correctAnswer: Int,
    val wrongAnswer: Int,
    val answeredQuestions: Int,
)

