package com.izhal.dicodingsubmission3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_follower.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
  private var following = ArrayList<User>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
    return FollowingViewHolder(view)
  }

  override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
    holder.bind(following[position])
  }

  override fun getItemCount(): Int = following.size

  fun setData(listUser: ArrayList<User>) {
    following.clear()
    following.addAll(listUser)
    notifyDataSetChanged()
  }

  inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) {
      itemView.imgFollower.loadImage(user.avatar_url)
      itemView.txtName.text = user.login
      itemView.setOnClickListener {
        val intent = Intent(itemView.context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_REPO_URL, user.html_url)
        itemView.context.startActivity(intent)
      }
    }
  }
}