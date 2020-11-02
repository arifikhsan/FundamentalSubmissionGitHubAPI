package com.example.fundamentalsubmissiongithubapi.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fundamentalsubmissiongithubapi.R
import com.example.fundamentalsubmissiongithubapi.model.User
import com.example.fundamentalsubmissiongithubapi.repository.GitHubRepository
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private val TAG = DetailActivity::class.java.simpleName
        private var username = ""
        private lateinit var user: User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        username = intent.getStringExtra(EXTRA_USERNAME) ?: "arifikhsan"
        initView()
        searchUserByUsername()
    }

    private fun searchUserByUsername() {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "df97872248fb3eecacba97569ad7156b9674c9df")
        client.addHeader("User-Agent", "request")
        detail_loading_indicator.visibility = View.VISIBLE

        client.get(
            "${GitHubRepository.BASE_URL}users/$username",
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    detail_loading_indicator.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    val userObject = JSONObject(result)

                    user = User(
                        id = userObject.getInt("id"),
                        login = userObject.getString("login"),
                        name = userObject.getString("name"),
                        avatarUrl = userObject.getString("avatar_url"),
                        htmlUrl = userObject.getString("html_url"),
                        type = userObject.getString("type"),
                        bio = userObject.getString("bio"),
                        follower = userObject.getInt("followers"),
                        following = userObject.getInt("following"),
                        publicRepos = userObject.getInt("public_repos"),
                        publicGists = userObject.getInt("public_gists"),
                    )
                    populateView()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    detail_loading_indicator.visibility = View.INVISIBLE
                    Log.d(TAG, "onFailure: $statusCode | ${error?.message}")
                    error?.printStackTrace()
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun populateView() {
        Glide.with(this).load(user.avatarUrl)
            .apply(RequestOptions().override(80, 80)).into(img_avatar)
        tv_name.text = user.name
        tv_username.text = "@${user.login}"
        if (user.bio == "null") {
            tv_bio.visibility = View.GONE
        } else {
            tv_bio.text = user.bio
        }
        tv_follower.text = "Followers: ${user.follower}"
        tv_following.text = "Following: ${user.following}"
        tv_public_repos.text = "${user.publicRepos} Public Repositories"
        tv_public_gists.text = "${user.publicGists} Public Gists"
    }

    private fun initView() {
        Log.d(TAG, "initView: $username")
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, username)
        view_pager.adapter = sectionsPagerAdapter

        tabs.setupWithViewPager(view_pager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val fragment = UserFollowFragment.newInstance(position + 1, username)
                fragment.fragmentBecameVisible()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Open in browser for ${user.htmlUrl}", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl)))
        }
    }
}