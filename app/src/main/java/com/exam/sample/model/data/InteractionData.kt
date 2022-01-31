package com.exam.sample.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * intent data
 */
@Parcelize
data class InteractionData(
    val userId: String,
    val type: String,
    val userName: String,
    val title: String,
    val urlEmbeded: String,
    val urlDownSized: String,
    val urlSmall: String
) : Parcelable



