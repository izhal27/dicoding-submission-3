package com.izhal.dicodingsubmission3

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.izhal.dicodingsubmission3.db.DatabaseContract.AUTHORITY
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.izhal.dicodingsubmission3.db.UserHelper

class UserProvider : ContentProvider() {
  companion object {
    private const val USER = 1
    private const val USER_ID = 2
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var userHelper: UserHelper

    init {
      uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
      uriMatcher.addURI(
        AUTHORITY,
        "$TABLE_NAME/#",
        USER_ID
      )
    }
  }

  override fun onCreate(): Boolean {
    userHelper = UserHelper.getInstance(context as Context)
    userHelper.open()
    return true
  }

  override fun query(
    uri: Uri, projection: Array<String>?, selection: String?,
    selectionArgs: Array<String>?, sortOrder: String?
  ): Cursor? {
    val cursor: Cursor?

    cursor = when (uriMatcher.match(uri)) {
      USER -> userHelper.getAll()
      USER_ID -> userHelper.getById(uri.lastPathSegment.toString())
      else -> null
    }

    return cursor
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    val added: Long = when (USER) {
      uriMatcher.match(uri) -> userHelper.insert(values)
      else -> 0
    }

    context?.contentResolver?.notifyChange(CONTENT_URI, null)

    return Uri.parse("$CONTENT_URI/$added")
  }

  override fun update(
    uri: Uri, values: ContentValues?, selection: String?,
    selectionArgs: Array<String>?
  ): Int {
    val updated: Int = when (USER_ID) {
      uriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(), values)
      else -> 0
    }

    context?.contentResolver?.notifyChange(CONTENT_URI, null)

    return updated
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    val deleted: Int = when (USER_ID) {
      uriMatcher.match(uri) -> userHelper.delete(uri.lastPathSegment.toString())
      else -> 0
    }

    context?.contentResolver?.notifyChange(CONTENT_URI, null)

    return deleted
  }
}