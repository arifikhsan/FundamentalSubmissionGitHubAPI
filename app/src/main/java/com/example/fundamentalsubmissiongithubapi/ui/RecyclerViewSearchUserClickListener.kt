package com.example.fundamentalsubmissiongithubapi.ui

import android.view.View
import com.example.fundamentalsubmissiongithubapi.model.User

interface RecyclerViewSearchUserClickListener {
    fun onItemClicked(view: View, user: User)
}