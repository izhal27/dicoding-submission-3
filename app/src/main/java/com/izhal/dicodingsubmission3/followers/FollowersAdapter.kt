package com.izhal.dicodingsubmission3.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.model.User
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.utils.loadImage
import kotlinx.android.synthetic.main.item_user.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
  private var followers = ArrayList<User>()
  private var onButtonDetailClickCallback: OnItemClickCallback<User>? = null
  private var onButtonRepoClickCallback: OnItemClickCallback<User>? = null

  fun setOnButtonDetailClickCallback(onItemClickCallback: OnItemClickCallback<User>) {
    this.onButtonDetailClickCallback = onItemClickCallback
  }

  fun setOnButtonRepoClickCallback(onItemClickCallback: OnItemClickCallback<User>) {
    this.onButtonRepoClickCallback = onItemClickCallback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
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
      itemView.imgUser.loadImage(user.avatarUrl)
      itemView.txtName.text = user.login
      itemView.btnOpenDetail.setOnClickListener{ onButtonDetailClickCallback?.onClicked(user)}
      itemView.btnOpenRepo.setOnClickListener{ onButtonRepoClickCallback?.onClicked(user)}
    }
  }
}