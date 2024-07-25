package com.gdalamin.bcs_pro.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
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