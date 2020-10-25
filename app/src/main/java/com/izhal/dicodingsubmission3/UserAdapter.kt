package com.izhal.dicodingsubmission3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izhal.dicodingsubmission3.model.User
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.utils.loadImage
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
  companion object {
    const val EXTRA_LOGIN = "extra_login"
  }

  private var users = ArrayList<User>()
  private var onButtonDetailClickCallback: OnItemClickCallback<User>? = null
  private var onButtonRepoClickCallback: OnItemClickCallback<User>? = null

  fun setOnButtonDetailClickCallback(onItemClickCallback: OnItemClickCallback<User>) {
    this.onButtonDetailClickCallback = onItemClickCallback
  }

  fun setOnButtonRepoClickCallback(onItemClickCallback: OnItemClickCallback<User>) {
    this.onButtonRepoClickCallback = onItemClickCallback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    return UserViewHolder(view)
  }

  override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
    holder.bind(users[position])
  }

  override fun getItemCount(): Int = users.size

  fun setData(listUser: ArrayList<User>) {
    users.clear()
    users.addAll(listUser)
    notifyDataSetChanged()
  }

  fun clearData() {
    users.clear()
    notifyDataSetChanged()
  }

  inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) {
      itemView.imgUser.loadImage(user.avatarUrl)
      itemView.txtName.text = user.login

      itemView.btnOpenDetail.setOnClickListener { onButtonDetailClickCallback?.onClicked(user) }
      itemView.btnOpenRepo.setOnClickListener {
        onButtonRepoClickCallback?.onClicked(user)
      }
    }
  }
}