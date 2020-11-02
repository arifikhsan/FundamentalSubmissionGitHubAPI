package com.example.fundamentalsubmissiongithubapi.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalsubmissiongithubapi.R
import com.example.fundamentalsubmissiongithubapi.model.User
import com.example.fundamentalsubmissiongithubapi.repository.GitHubRepository
import com.example.fundamentalsubmissiongithubapi.ui.detail.DetailActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), RecyclerViewUserClickListener {
    private var searchUsername = "arif"
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
        sv_search_user.setOnClickListener { sv_search_user.isIconified = false }
        sv_search_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = false
            override fun onQueryTextSubmit(query: String?): Boolean {
                loading_indicator.visibility = View.VISIBLE
                searchUserResult.clear()
                sv_search_user.clearFocus()
                searchUsername = query.toString()
                searchUserByUsername()
                return true
            }
        })
    }

    private fun searchUserByUsername() {
        tv_begin_search.visibility = View.INVISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "df97872248fb3eecacba97569ad7156b9674c9df")
        client.addHeader("User-Agent", "request")

        client.get(
            "${GitHubRepository.BASE_URL}search/users?q=$searchUsername",
            object : AsyncHttpResponseHandler() {
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
                            id = userObject.getInt("id"),
                            login = userObject.getString("login"),
                            name = userObject.getString("login"),
                            avatarUrl = userObject.getString("avatar_url"),
                            type = userObject.getString("type")
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
//        Toast.makeText(this@MainActivity, "Get detail for ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
        startActivity(intent)
    }
}