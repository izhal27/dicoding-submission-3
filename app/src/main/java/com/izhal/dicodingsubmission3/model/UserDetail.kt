package com.izhal.dicodingsubmission3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface IUserDetail {
  val id: Int
  val name: String
  val reposUrl: String
  val followersUrl: String
  val followingUrl: String
  val location: String
  val bio: String
  val followers: Int
  val following: Int
}

@Parcelize
class UserDetail(
  override val id: Int,
  override val g_id: Int,
  override val login: String,
  override val avatarUrl: String,
  override val url: String,
  override val htmlUrl: String,
  override val name: String,
  override val reposUrl: String,
  override val followersUrl: String,
  override val followingUrl: String,
  override val location: String,
  override val bio: String,
  override val followers: Int,
  override val following: Int
) : IUser, IUserDetail, Parcelable