package com.izhal.dicodingsubmission3

interface IUserDetail {
  val name: String
  val repos_url: String
  val followers_url: String
  val following_url: String
  val location: String
  val bio: String
  val followers: Int
  val following: Int
}

class UserDetail(
  override val id: Int,
  override val login: String,
  override val avatar_url: String,
  override val url: String,
  override val html_url: String,
  override val name: String,
  override val repos_url: String,
  override val followers_url: String,
  override val following_url: String,
  override val location: String,
  override val bio: String,
  override val followers: Int,
  override val following: Int
) : IUser, IUserDetail