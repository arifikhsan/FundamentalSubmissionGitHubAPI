package com.example.fundamentalsubmissiongithubapi.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fundamentalsubmissiongithubapi.R
import com.example.fundamentalsubmissiongithubapi.model.User
import kotlinx.android.synthetic.main.item_search_user.view.*

class SearchUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {
    var clickListener: RecyclerViewUserClickListener? = null

    class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                tv_item_username.text = user.login
                tv_item_name.text = user.type
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(55, 55)).into(img_avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        return SearchUserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener {
            clickListener?.onItemClicked(it, listUser[position])
        }
    }

    override fun getItemCount(): Int = listUser.size
}