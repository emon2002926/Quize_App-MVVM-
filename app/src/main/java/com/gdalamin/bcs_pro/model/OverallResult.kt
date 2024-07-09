package com.gdalamin.bcs_pro.model

data class OverallResult(

    val answeredQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val mark: Double,

    )