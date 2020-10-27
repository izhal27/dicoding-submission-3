package com.izhal.dicodingsubmission3.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(url: String?) {
  Glide.with(this.context)
    .load(url)
    .apply(RequestOptions().override(500, 500))
    .centerCrop()
    .into(this)
}

interface OnItemClickCallback<T> {
  fun onClicked(data: T)
}

enum class STATUS_FOLLOW {
  FOLLOWERS,
  FOLLOWING
}