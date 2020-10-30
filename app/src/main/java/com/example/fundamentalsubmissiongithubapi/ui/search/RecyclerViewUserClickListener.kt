package com.example.fundamentalsubmissiongithubapi.ui.search

import android.view.View
import com.example.fundamentalsubmissiongithubapi.model.User

interface RecyclerViewUserClickListener {
    fun onItemClicked(view: View, user: User)
}