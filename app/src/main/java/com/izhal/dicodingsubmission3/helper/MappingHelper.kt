package com.izhal.dicodingsubmission3.helper

import android.database.Cursor
import com.izhal.dicodingsubmission3.db.DatabaseContract
import com.izhal.dicodingsubmission3.model.UserDetail

object MappingHelper {
  private fun getUser(cursor: Cursor?): UserDetail? {
    if (cursor != null && cursor.moveToFirst()) {
      cursor.apply {
        val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
        val g_id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.G_ID))
        val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
        val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
        val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL))
        val htmlUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.HTML_URL))
        val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
        val reposUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOS_URL))
        val followersUrl =
          getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS_URL))
        val followingUrl =
          getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING_URL))
        val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
        val bio = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.BIO))
        val followers = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
        val following = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))

        return UserDetail(
          id,
          g_id,
          login,
          avatarUrl,
          url,
          htmlUrl,
          name,
          reposUrl,
          followersUrl,
          followingUrl,
          location,
          bio,
          followers,
          following
        )
      }
    }

    return null
  }

  fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserDetail> {
    val usersList = ArrayList<UserDetail>()

    usersCursor?.apply {
      while (moveToNext()) {
        getUser(this)?.let { usersList.add(it) }
      }
    }

    return usersList
  }

  fun mapCursorToObject(usersCursor: Cursor?): UserDetail? {
    var user: UserDetail? = null

    usersCursor?.apply {
      user = getUser(this)
    }

    return user
  }
}