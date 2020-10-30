package com.example.fundamentalsubmissiongithubapi.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalsubmissiongithubapi.R
import com.example.fundamentalsubmissiongithubapi.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), RecyclerViewSearchUserClickListener {
    private var searchUsername = "aaa"
    private val baseUrl = "https://api.github.com/"
    private var searchUserResult = ArrayList<User>()

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
                loading_indicator.visibility = View.VISIBLE
                searchUserResult.clear()
                sv_search_user.clearFocus()
                searchUsername = query.toString()
                Toast.makeText(this@MainActivity, "Searching for $query", Toast.LENGTH_SHORT).show()
                searchUserByUsername()
                return true
            }
        })
    }

    private fun searchUserByUsername() {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "df97872248fb3eecacba97569ad7156b9674c9df")
        client.addHeader("User-Agent", "request")

        client.get("${baseUrl}search/users?q=$searchUsername", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                loading_indicator.visibility = View.INVISIBLE
                val result = String(responseBody)

                val jsonObject = JSONObject(result)
                val items = jsonObject.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val userObject = items.getJSONObject(i)
                    val user = User(
                        userObject.getInt("id"),
                        userObject.getString("login"),
                        userObject.getString("login"),
                        userObject.getString("avatar_url"),
                        userObject.getString("type")
                    )
                    searchUserResult.add(user)
                }
                showSearchUser()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                loading_indicator.visibility = View.INVISIBLE
                Log.d(TAG, "onFailure: $statusCode | ${error?.message}")
                error?.printStackTrace()
            }
        })

    }

    private fun showSearchUser() {
        val userAdapter = SearchUserAdapter(searchUserResult)
        rv_search_result.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
        userAdapter.clickListener = this
    }

    override fun onItemClicked(view: View, user: User) {
        Toast.makeText(this@MainActivity, "Get detail for ${user.login}", Toast.LENGTH_SHORT).show()
    }
}