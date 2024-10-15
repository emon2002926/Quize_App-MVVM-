package com.gdalamin.bcs_pro.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
@Entity
data class BcsYearName(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int,
    val bcsYearName: String,
    val id: Int,
    val totalQuestion: String,
    val isQuestionSaved: Boolean,
    var isDownloading: Boolean = false, // New flag to track animation state
    var isAnimated: Boolean = false  // Add this property


) : Parcelable