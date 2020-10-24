package com.izhal.dicodingsubmission3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

internal class DatabaseContract {
  internal class UserColumns : BaseColumns {
    companion object {
      const val TABLE_NAME = "users"
      const val _ID = "_id"
      const val G_ID = "g_id" // github id
      const val LOGIN = "login" // login id
      const val AVATAR_URL = "avatar_url"
      const val URL = "url"
      const val HTML_URL = "html_url"
      const val NAME = "name"
      const val REPOS_URL = "repos_url"
      const val FOLLOWERS_URL = "followers_url"
      const val FOLLOWING_URL = "following_url"
      const val LOCATION = "location"
      const val BIO = "bio"
      const val FOLLOWERS = "followers"
      const val FOLLOWING = "following"
    }
  }

  internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
      private const val DATABASE_NAME = "db_github_users"
      private const val DATABASE_VERSION = 1
      private const val SQL_CREATE_TABLE_USERS = "CREATE TABLE ${UserColumns.TABLE_NAME} " +
              "(${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
              "${UserColumns.G_ID} INTEGER NOT NULL, " +
              "${UserColumns.LOGIN} TEXT NOT NULL, " +
              "${UserColumns.AVATAR_URL} TEXT NOT NULL, " +
              "${UserColumns.URL} TEXT NOT NULL, " +
              "${UserColumns.HTML_URL} TEXT NOT NULL, " +
              "${UserColumns.NAME} TEXT DEFAULT \"Unknown\" NOT NULL, " +
              "${UserColumns.REPOS_URL} TEXT NOT NULL, " +
              "${UserColumns.FOLLOWERS_URL} TEXT NOT NULL, " +
              "${UserColumns.FOLLOWING_URL} TEXT NOT NULL, " +
              "${UserColumns.LOCATION} TEXT DEFAULT \"Unknown\" NOT NULL, " +
              "${UserColumns.BIO} TEXT DEFAULT \"Unknown\" NOT NULL, " +
              "${UserColumns.FOLLOWERS} INTEGER NOT NULL, " +
              "${UserColumns.FOLLOWING} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
      db?.execSQL(SQL_CREATE_TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
      db?.execSQL("DROP TABLE IF EXISTS ${UserColumns.TABLE_NAME}")
      onCreate(db)
    }
  }
}