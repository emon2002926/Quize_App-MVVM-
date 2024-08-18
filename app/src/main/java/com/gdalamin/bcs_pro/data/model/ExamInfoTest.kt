package com.gdalamin.bcs_pro.data.model

data class ExamInfoTest(
    val subjectName: String,
    var totalSelectedAnswer: Int = 0,
    var totalCorrectAnswer: Int = 0,
    var totalWrongAnswer: Int = 0,
    var totalMark: Double = 0.0
)
