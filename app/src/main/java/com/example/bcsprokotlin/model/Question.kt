package com.example.bcsprokotlin.model

data class Question(
    val answer: String,
    val batch: String,
    val explanation: String,
    val explanationImage: String,
    val id: String,
    val image: String,
    val option1: String,
    val option1Image: String,
    val option2: String,
    val option2Image: String,
    val option3: String,
    val option3Image: String,
    val option4: String,
    val option4Image: String,
    val question: String,
    val subjects: String,
    var userSelectedAnswer: Int = 0
)