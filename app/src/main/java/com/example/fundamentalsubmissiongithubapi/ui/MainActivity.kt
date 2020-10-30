package com.example.fundamentalsubmissiongithubapi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.fundamentalsubmissiongithubapi.R
import com.example.fundamentalsubmissiongithubapi.repository.GitHubRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var searchQuery = "aaa"
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        sv_search_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = false
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query.toString()
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                GitHubRepository().searchUser(searchQuery)
                return true
            }
        })
    }
}