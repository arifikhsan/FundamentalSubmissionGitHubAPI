package com.example.fundamentalsubmissiongithubapi.repository

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class GitHubRepository {
    private val baseUrl = "https://api.github.com/"

    companion object {
        private val TAG = GitHubRepository::class.java.simpleName
    }

    fun searchUser(username: String): ArrayList<String> {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "df97872248fb3eecacba97569ad7156b9674c9df")
        client.addHeader("User-Agent", "request")
        val items = ArrayList<String>()

        client.get("${baseUrl}search/users?q=$username", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val username = jsonObject.getString("login")
                    items.add(username)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure: $statusCode | ${error?.message}")
                error?.printStackTrace()
            }
        })

        return items
    }
}