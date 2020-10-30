package com.example.fundamentalsubmissiongithubapi.repository

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class GitHubRepository {
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}