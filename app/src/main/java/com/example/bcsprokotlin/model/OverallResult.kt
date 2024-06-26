package com.example.bcsprokotlin.model

data class OverallResult(
    val answeredQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val mark: Double
)