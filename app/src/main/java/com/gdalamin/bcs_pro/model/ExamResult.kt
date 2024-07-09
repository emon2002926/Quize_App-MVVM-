package com.gdalamin.bcs_pro.model

data class ExamResult(
    val subjectName: String,
    val mark: Double,
    val correctAnswer: Int,
    val wrongAnswer: Int,
    val answeredQuestions: Int,
)

