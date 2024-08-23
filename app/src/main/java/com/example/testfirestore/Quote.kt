package com.example.testfirestore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quote(
    val userId: String = "",
    val nama: String = "",
    val noHp: String = "",
    val jenisK: String = "",
    val tanggal: String = "",
    val quote: String = "",
    val timestamp: Long = 0
) : Parcelable
