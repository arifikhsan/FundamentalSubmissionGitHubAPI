package com.example.fundamentalsubmissiongithubapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val id: Int, val login: String, val name: String) : Parcelable