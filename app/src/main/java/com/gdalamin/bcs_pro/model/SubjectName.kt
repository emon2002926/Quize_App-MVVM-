package com.gdalamin.bcs_pro.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class SubjectName(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int,
    val end_color: String,
    val icon_image_string: String,
    val id: Int,
    val start_color: String,
    val subject_code: String,
    val subject_name: String
) : Parcelable