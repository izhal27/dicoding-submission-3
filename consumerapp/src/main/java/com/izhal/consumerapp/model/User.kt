package com.izhal.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface IUser {
  val g_id: Int
  val login: String
  val avatarUrl: String
  val url: String
  val htmlUrl: String
}
@Parcelize
class User(
  override val g_id: Int,
  override val login: String,
  override val avatarUrl: String,
  override val url: String,
  override val htmlUrl: String
) : IUser, Parcelable