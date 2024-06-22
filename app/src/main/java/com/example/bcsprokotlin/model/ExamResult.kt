package com.example.bcsprokotlin.model

data class ExamResult(
    var subjectName: String,
    var mark: Double,
    var correctAnswer: Int,
    var wrongAnswer: Int,
    var answeredQuestions: Int,
)

