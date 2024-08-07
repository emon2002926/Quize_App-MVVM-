package com.gdalamin.bcs_pro.data.model

import androidx.annotation.Keep

@Keep
data class OverallResult(

    val answeredQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val mark: Double,

    )