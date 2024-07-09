package com.gdalamin.bcs_pro.model

data class SharedData(
    val title: String,
    val action: String,
    val totalQuestion: Int,
    val questionType: String,
    val batchOrSubjectName: String,
    val time: Int
)
