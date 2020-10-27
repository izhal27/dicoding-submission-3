package com.izhal.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izhal.consumerapp.model.UserDetail
import com.izhal.consumerapp.utils.OnItemClickCallback
import com.izhal.consumerapp.utils.loadImage
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.FavoritesHolder>() {
  private var listUserDetails = ArrayList<UserDetail>()
  private var onButtonRepoClickCallback: OnItemClickCallback<UserDetail>? = null

  fun setOnButtonDeleteClickCallback(onItemClickCallback: OnItemClickCallback<UserDetail>) {
    this.onButtonRepoClickCallback = onItemClickCallback
  }

  fun setUserDetails(userDetails: ArrayList<UserDetail>) {
    listUserDetails.clear()
    listUserDetails = userDetails
    notifyDataSetChanged()
  }

  fun clearData() {
    listUserDetails.clear()
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    return FavoritesHolder(view)
  }

  override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
    holder.bind(listUserDetails[position])
  }

  override fun getItemCount(): Int = listUserDetails.size

  inner class FavoritesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(userDetail: UserDetail) {
      itemView.imgUser.loadImage(userDetail.avatarUrl)
      itemView.txtName.text = userDetail.login

      itemView.btnDelete.setOnClickListener {
        onButtonRepoClickCallback?.onClicked(userDetail)
      }
    }
  }
}