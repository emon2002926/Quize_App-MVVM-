package com.example.bcsprokotlin.model

data class ExamResult(
    val subjectName: String,
    val mark: Double,
    val correctAnswer: Int,
    val wrongAnswer: Int,
    val answeredQuestions: Int,
)

