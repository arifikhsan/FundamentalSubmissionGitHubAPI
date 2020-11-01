package com.example.fundamentalsubmissiongithubapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatarUrl: String,
    val type: String,
    val bio: String = "",
    val htmlUrl: String = "",
    val follower: Int = 0,
    val following: Int = 0,
    val publicRepos: Int = 0,
    val publicGists: Int = 0,
) :
    Parcelable