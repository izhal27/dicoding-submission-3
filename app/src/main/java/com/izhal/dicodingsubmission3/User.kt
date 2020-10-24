package com.izhal.dicodingsubmission3

interface IUser {
  val id: Int
  val login: String
  val avatar_url: String
  val url: String
  val html_url: String
}

class User(
  override val id: Int,
  override val login: String,
  override val avatar_url: String,
  override val url: String,
  override val html_url: String
) : IUser