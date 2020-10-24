package com.izhal.dicodingsubmission3.followers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.WebViewActivity
import com.izhal.dicodingsubmission3.utils.loadImage
import com.izhal.dicodingsubmission3.model.User
import kotlinx.android.synthetic.main.item_follower.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
  private var followers = ArrayList<User>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
    return FollowersViewHolder(view)
  }

  override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
    holder.bind(followers[position])
  }

  override fun getItemCount(): Int = followers.size

  fun setData(listUser: ArrayList<User>) {
    followers.clear()
    followers.addAll(listUser)
    notifyDataSetChanged()
  }

  inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) {
      itemView.imgFollower.loadImage(user.avatarUrl)
      itemView.txtName.text = user.login
      itemView.setOnClickListener {
        val intent = Intent(itemView.context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_REPO_URL, user.htmlUrl)
        itemView.context.startActivity(intent)
      }
    }
  }
}