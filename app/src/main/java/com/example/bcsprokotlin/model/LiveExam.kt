package com.example.bcsprokotlin.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class LiveExam(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int,
    val dailyExam: String,
    val details: String,
    val id: Int,
    val totalQc: Int
) : Parcelable