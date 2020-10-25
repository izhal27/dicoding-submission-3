package com.izhal.dicodingsubmission3.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.model.UserDetail
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.utils.loadImage
import kotlinx.android.synthetic.main.item_user.view.*

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder>() {
  private var listUserDetails = ArrayList<UserDetail>()
  private var onButtonDetailClickCallback: OnItemClickCallback<UserDetail>? = null
  private var onButtonRepoClickCallback: OnItemClickCallback<UserDetail>? = null

  fun setOnButtonDetailClickCallback(onItemClickCallback: OnItemClickCallback<UserDetail>) {
    this.onButtonDetailClickCallback = onItemClickCallback
  }

  fun setOnButtonRepoClickCallback(onItemClickCallback: OnItemClickCallback<UserDetail>) {
    this.onButtonDetailClickCallback = onItemClickCallback
  }

  fun setUserDetails(userDetails: ArrayList<UserDetail>) {
    listUserDetails = userDetails
  }

  fun clearUserDetails() {
    listUserDetails.clear()
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
      itemView.imgAvatarDetail.loadImage(userDetail.avatarUrl)
      itemView.txtName.text = userDetail.login

      itemView.btnOpenDetail.setOnClickListener { onButtonDetailClickCallback?.onClicked(userDetail) }
      itemView.btnOpenRepo.setOnClickListener {
        onButtonRepoClickCallback?.onClicked(userDetail)
      }
    }
  }
}